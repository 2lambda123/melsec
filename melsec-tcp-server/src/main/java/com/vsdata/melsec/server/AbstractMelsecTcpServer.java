package com.vsdata.melsec.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liumin
 */
public abstract class AbstractMelsecTcpServer implements MelsecTcpServer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<SocketAddress, Channel> serverChannels = new ConcurrentHashMap<>();

    private final MelsecTcpServerConfig config;

    public AbstractMelsecTcpServer(MelsecTcpServerConfig config) {
        this.config = config;
    }

    @Override
    public CompletableFuture<MelsecTcpServer> bind(String host, int port) {
        CompletableFuture<MelsecTcpServer> bindFuture = new CompletableFuture<>();

        ServerBootstrap bootstrap = new ServerBootstrap();

        ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                logger.info("channel initialized: {}", channel);
                channel.pipeline().addLast(new LoggingHandler(LogLevel.TRACE));
                AbstractMelsecTcpServer.this.initChannel(channel.pipeline());
                channel.pipeline().addLast(new ServerFrameEMessageHandler());
            }
        };

        config.getBootstrapConsumer().accept(bootstrap);

        bootstrap.group(config.getEventLoop())
            .channel(NioServerSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.DEBUG))
            .childHandler(initializer)
            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        bootstrap.bind(host, port).addListener((ChannelFuture future) -> {
            if (future.isSuccess()) {
                Channel channel = future.channel();
                serverChannels.put(channel.localAddress(), channel);
                bindFuture.complete(AbstractMelsecTcpServer.this);
            } else {
                bindFuture.completeExceptionally(future.cause());
            }
        });

        return bindFuture;
    }

    /**
     * 初始化Channel，增加相应的编解码器
     *
     * @param pipeline ChannelPipeline
     */
    protected abstract void initChannel(ChannelPipeline pipeline);

    @Override
    public void shutdown() {
        serverChannels.values().forEach(Channel::close);
        serverChannels.clear();
    }
}
