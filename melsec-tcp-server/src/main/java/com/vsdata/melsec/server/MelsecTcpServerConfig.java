package com.vsdata.melsec.server;

import com.vsdata.melsec.Melsec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.util.HashedWheelTimer;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * @author liumin
 */
public class MelsecTcpServerConfig {

    private final ExecutorService executor;
    private final EventLoopGroup eventLoop;
    private final HashedWheelTimer wheelTimer;
    private final Consumer<ServerBootstrap> bootstrapConsumer;

    public MelsecTcpServerConfig(ExecutorService executor,
                                 EventLoopGroup eventLoop,
                                 HashedWheelTimer wheelTimer,
                                 Consumer<ServerBootstrap> bootstrapConsumer) {
        this.executor = executor;
        this.eventLoop = eventLoop;
        this.wheelTimer = wheelTimer;
        this.bootstrapConsumer = bootstrapConsumer;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public EventLoopGroup getEventLoop() {
        return eventLoop;
    }

    public HashedWheelTimer getWheelTimer() {
        return wheelTimer;
    }

    public Consumer<ServerBootstrap> getBootstrapConsumer() {
        return bootstrapConsumer;
    }

    public static class Builder {
        private ExecutorService executor;
        private EventLoopGroup eventLoop;
        private HashedWheelTimer wheelTimer;
        private Consumer<ServerBootstrap> bootstrapConsumer = (b) -> {
        };

        public Builder setExecutor(ExecutorService executor) {
            this.executor = executor;
            return this;
        }

        public Builder setEventLoop(EventLoopGroup eventLoop) {
            this.eventLoop = eventLoop;
            return this;
        }

        public Builder setWheelTimer(HashedWheelTimer wheelTimer) {
            this.wheelTimer = wheelTimer;
            return this;
        }

        public Builder setBootstrapConsumer(Consumer<ServerBootstrap> consumer) {
            this.bootstrapConsumer = consumer;
            return this;
        }

        public MelsecTcpServerConfig build() {
            return new MelsecTcpServerConfig(
                executor != null ? executor : Melsec.sharedExecutor(),
                eventLoop != null ? eventLoop : Melsec.sharedEventLoop(),
                wheelTimer != null ? wheelTimer : Melsec.sharedWheelTimer(),
                bootstrapConsumer);
        }
    }
}
