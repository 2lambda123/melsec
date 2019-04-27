package com.vsdata.melsec.client;

import com.vsdata.melsec.MelsecTimeoutException;
import com.vsdata.melsec.message.Function;
import com.vsdata.melsec.message.UnitType;
import com.vsdata.melsec.message.e.FrameECommand;
import com.vsdata.melsec.message.e.FrameEResponse;
import com.vsdata.melsec.utils.BinaryConverters;
import com.vsdata.melsec.utils.ByteBufUtilities;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liumin
 */
public abstract class AbstractTcpClient implements MelsecTcpClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final SynchronousQueue<PendingRequest<? extends FrameEResponse>>
        pendingRequestQueue = new SynchronousQueue<>();

    private final ChannelManager channelManager;

    protected final MelsecClientConfig config;

    private final Lock lock;

    public AbstractTcpClient(MelsecClientConfig config) {
        this.config = config;
        channelManager = new ChannelManager(this);
        lock = new ReentrantLock();
    }

    @Override
    public CompletableFuture<Channel> bootstrap() {
        CompletableFuture<Channel> future = new CompletableFuture<>();
        Bootstrap bootstrap = new Bootstrap();
        config.getBootstrapConsumer().accept(bootstrap);
        bootstrap.group(config.getEventLoop())
            .channel(NioSocketChannel.class)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) config.getTimeout().toMillis())
            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    AbstractTcpClient.this.initChannel(ch.pipeline());
                    ch.pipeline().addLast(new MelsecClientHandler(AbstractTcpClient.this));
                }
            })
            .connect(config.getAddress(), config.getPort())
            .addListener((ChannelFuture f) -> {
                if (f.isSuccess()) {
                    future.complete(f.channel());
                } else {
                    future.completeExceptionally(f.cause());
                }
            });

        return future;
    }

    /**
     * 初始化Channel，增加相应的编解码器
     *
     * @param pipeline ChannelPipeline
     * @throws Exception 异常
     */
    protected abstract void initChannel(ChannelPipeline pipeline) throws Exception;

    private static class MelsecClientHandler extends SimpleChannelInboundHandler<FrameEResponse> {

        private MelsecTcpClient client;

        private MelsecClientHandler(MelsecTcpClient client) {
            this.client = client;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, FrameEResponse response) throws Exception {
            client.onChannelRead(channelHandlerContext, response);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            client.exceptionCaught(ctx, cause);
        }
    }

    @Override
    public CompletableFuture<MelsecTcpClient> connect() {
        CompletableFuture<MelsecTcpClient> future = new CompletableFuture<>();

        channelManager.getChannel().whenComplete((ch, ex) -> {
            if (ch != null) {
                future.complete(AbstractTcpClient.this);
            } else {
                future.completeExceptionally(ex);
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<MelsecTcpClient> disconnect() {
        return channelManager.disconnect().thenApply(v -> this);
    }

    @Override
    public <T extends FrameEResponse> CompletableFuture<T> sendRequest(FrameECommand command) {
        CompletableFuture<T> future = new CompletableFuture<>();
        channelManager.getChannel().whenCompleteAsync((ch, ex) -> {
            if (ch != null) {
                // 获取锁，使得同一时间内只能发送一个请求
                lock.lock();
                try {
                    PendingRequest<? extends FrameEResponse> pendingRequest = new PendingRequest<>(command, future);
                    ch.writeAndFlush(command).addListener(f -> {
                        if (!f.isSuccess()) {
                            pendingRequestQueue.poll();
                            pendingRequest.promise.completeExceptionally(f.cause());
                            pendingRequest.timeout.cancel();
                        }
                    });
                    pendingRequestQueue.put(pendingRequest);
                } catch (InterruptedException e) {
                    // 放入队列时被打断
                    future.completeExceptionally(e);
                } finally {
                    // 发送完成返回后释放锁
                    lock.unlock();
                }
            } else {
                future.completeExceptionally(ex);
            }
        });

        return future;
    }

    @Override
    public void onChannelRead(ChannelHandlerContext ctx, FrameEResponse response) {
        config.getExecutor().submit(() -> handleResponse(response));
    }

    private void handleResponse(FrameEResponse response) {
        PendingRequest<?> pending = pendingRequestQueue.poll();
        if (pending != null) {
            pending.timeout.cancel();
            // Data conversion
            if (pending.command.getPrincipal().getFunction() == Function.BATCH_READ) {
                if (pending.command.getPrincipal().getDevice().getType() == UnitType.BIT) {
                    byte[] bytes = BinaryConverters.convertBinaryOnBitToBoolArray(
                        ByteBufUtilities.readAllBytes(response.getData()),
                        pending.command.getPrincipal().getPoints());
                    response.setData(Unpooled.wrappedBuffer(bytes));
                } else {
                    int remaining = response.getData().readableBytes();
                    ByteBuf data = Unpooled.buffer(remaining);
                    ByteBufUtilities.swapLEToBE(data, response.getData());
                    response.setData(data);
                }
            }
            pending.promise.complete(response);
        } else {
            ReferenceCountUtil.release(response.getData());
            logger.debug("Received response for unknown response: {}", response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        failPendingRequests(cause);
        ctx.close();
        onExceptionCaught(ctx, cause);
    }

    /**
     * Logs the exception on DEBUG level.
     * <p>
     * Subclasses may override to customize logging behavior.
     *
     * @param ctx   the {@link ChannelHandlerContext}.
     * @param cause the exception that was caught.
     */
    @SuppressWarnings("WeakerAccess")
    protected void onExceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.debug("Exception caught: {}", cause.getMessage(), cause);
    }

    private void failPendingRequests(Throwable cause) {
        try {
            lock.lock();
            pendingRequestQueue.forEach(p -> p.promise.completeExceptionally(cause));
            pendingRequestQueue.poll();
        } finally {
            lock.unlock();
        }
    }

    private class PendingRequest<T> {
        private final CompletableFuture<FrameEResponse> promise = new CompletableFuture<>();

        private final Timeout timeout;

        private FrameECommand command;

        @SuppressWarnings("unchecked")
        private PendingRequest(FrameECommand command, CompletableFuture<T> future) {
            this.command = command;

            this.timeout = config.getWheelTimer().newTimeout(t -> {
                if (t.isCancelled()) {
                    return;
                }
                pendingRequestQueue.poll();
                promise.completeExceptionally(new MelsecTimeoutException(config.getTimeout()));

            }, config.getTimeout().getSeconds(), TimeUnit.SECONDS);

            promise.whenComplete((r, ex) -> {
                if (r != null) {
                    try {
                        future.complete((T) r);
                    } catch (ClassCastException e) {
                        future.completeExceptionally(e);
                    }
                } else {
                    future.completeExceptionally(ex);
                }
            });
        }
    }
}
