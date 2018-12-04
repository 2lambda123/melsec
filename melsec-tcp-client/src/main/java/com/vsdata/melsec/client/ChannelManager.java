package com.vsdata.melsec.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

/**
 * @author liumin
 */
public class ChannelManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicReference<State> state = new AtomicReference<>(new Idle());

    private final MelsecTcpClient client;

    public ChannelManager(MelsecTcpClient client) {
        this.client = client;
    }

    CompletableFuture<Channel> getChannel() {
        State currentState = state.get();

        if (currentState instanceof Idle) {
            Connecting nextState = new Connecting();

            if (state.compareAndSet(currentState, nextState)) {
                CompletableFuture<Channel> future = nextState.future;

                future.whenComplete((ch, ex) -> {
                    if (ch != null) {
                        state.set(new Connected(future));
                    } else {
                        state.set(new Idle());
                    }
                });

                return connect(future);
            } else {
                return getChannel();
            }
        } else if (currentState instanceof Connecting) {
            return ((Connecting) currentState).future;
        } else if (currentState instanceof Connected) {
            return ((Connected) currentState).future;
        } else {
            throw new IllegalStateException(currentState.getClass().getSimpleName());
        }
    }

    private CompletableFuture<Channel> connect(CompletableFuture<Channel> future) {
        CompletableFuture<Channel> bootstrap = client.bootstrap();

        bootstrap.whenComplete((ch, ex) -> {
            if (ch != null) {
                logger.debug(
                    "Channel bootstrap succeeded: localAddress={}, remoteAddress={}",
                    ch.localAddress(), ch.remoteAddress());

                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        State currentState = state.get();

                        if (currentState instanceof Connected) {
                            if (state.compareAndSet(currentState, new Idle())) {
                                logger.debug("channelInactive(), transitioned to Idle");
                            }
                        }

                        super.channelInactive(ctx);
                    }
                });

                future.complete(ch);
            } else {
                logger.debug("Channel bootstrap failed: {}", ex.getMessage(), ex);

                future.completeExceptionally(ex);
            }
        });

        return future;
    }

    CompletableFuture<Void> disconnect() {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        final State currentState = state.get();

        BiConsumer<Channel, Throwable> disconnect = (ch, ex) -> {
            state.compareAndSet(currentState, new Idle());

            if (ch != null) {
                ch.pipeline().addFirst(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        logger.debug("channelInactive(), disconnect complete");
                        future.complete(null);
                    }
                });

                ch.close();
            } else {
                future.complete(null);
            }
        };


        if (currentState instanceof Idle) {
            future.complete(null);
        } else if (currentState instanceof Connecting) {
            ((Connecting) currentState).future.whenComplete(disconnect);
        } else if (currentState instanceof Connected) {
            ((Connected) currentState).future.whenComplete(disconnect);
        }

        return future;
    }

    private static abstract class State {
    }

    private static class Idle extends State {
    }

    private static class Connecting extends State {
        private final CompletableFuture<Channel> future = new CompletableFuture<>();
    }

    private static class Connected extends State {
        private final CompletableFuture<Channel> future;

        private Connected(CompletableFuture<Channel> future) {
            this.future = future;
        }
    }
}
