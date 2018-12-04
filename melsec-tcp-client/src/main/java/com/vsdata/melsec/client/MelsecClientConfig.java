package com.vsdata.melsec.client;

import com.vsdata.melsec.Melsec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.util.HashedWheelTimer;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * @author liumin
 */
public class MelsecClientConfig {

    private final String address;
    private final int port;
    private final Duration timeout;
    private final ExecutorService executor;
    private final EventLoopGroup eventLoop;
    private final HashedWheelTimer wheelTimer;
    private final Consumer<Bootstrap> bootstrapConsumer;

    private final int networkNo;
    private final int pcNo;
    private final int requestDestinationModuleIoNo;
    private final int requestDestinationModuleStationNo;

    public MelsecClientConfig(String address,
                              int port,
                              Duration timeout,
                              ExecutorService executor,
                              EventLoopGroup eventLoop,
                              HashedWheelTimer wheelTimer,
                              Consumer<Bootstrap> bootstrapConsumer,
                              int networkNo, int pcNo,
                              int requestDestinationModuleIoNo,
                              int requestDestinationModuleStationNo) {
        this.address = address;
        this.port = port;
        this.timeout = timeout;
        this.executor = executor;
        this.eventLoop = eventLoop;
        this.wheelTimer = wheelTimer;
        this.bootstrapConsumer = bootstrapConsumer;
        this.networkNo = networkNo;
        this.pcNo = pcNo;
        this.requestDestinationModuleIoNo = requestDestinationModuleIoNo;
        this.requestDestinationModuleStationNo = requestDestinationModuleStationNo;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public Duration getTimeout() {
        return timeout;
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

    public Consumer<Bootstrap> getBootstrapConsumer() {
        return bootstrapConsumer;
    }

    public int getNetworkNo() {
        return networkNo;
    }

    public int getPcNo() {
        return pcNo;
    }

    public int getRequestDestinationModuleIoNo() {
        return requestDestinationModuleIoNo;
    }

    public int getRequestDestinationModuleStationNo() {
        return requestDestinationModuleStationNo;
    }

    public static class Builder {

        private final String address;

        private int port = 6000;
        private Duration timeout = Duration.ofSeconds(5);
        private ExecutorService executor;
        private EventLoopGroup eventLoop;
        private HashedWheelTimer wheelTimer;
        private Consumer<Bootstrap> bootstrapConsumer = (b) -> {
        };
        private int networkNo = 0x00;
        private int pcNo = 0xFF;
        private int requestDestinationModuleIoNo = 0x03FF;
        private int requestDestinationModuleStationNo = 0x00;

        public Builder(String address) {
            this.address = address;
        }

        public Builder(String address, int port) {
            this.address = address;
            this.port = port;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setTimeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

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

        public Builder setBootstrapConsumer(Consumer<Bootstrap> consumer) {
            this.bootstrapConsumer = consumer;
            return this;
        }

        public void setNetworkNo(int networkNo) {
            this.networkNo = networkNo;
        }

        public void setPcNo(int pcNo) {
            this.pcNo = pcNo;
        }

        public void setRequestDestinationModuleIoNo(int requestDestinationModuleIoNo) {
            this.requestDestinationModuleIoNo = requestDestinationModuleIoNo;
        }

        public void setRequestDestinationModuleStationNo(int requestDestinationModuleStationNo) {
            this.requestDestinationModuleStationNo = requestDestinationModuleStationNo;
        }

        public MelsecClientConfig build() {
            return new MelsecClientConfig(
                address,
                port,
                timeout,
                executor != null ? executor : Melsec.sharedExecutor(),
                eventLoop != null ? eventLoop : Melsec.sharedEventLoop(),
                wheelTimer != null ? wheelTimer : Melsec.sharedWheelTimer(),
                bootstrapConsumer,
                networkNo,
                pcNo,
                requestDestinationModuleIoNo,
                requestDestinationModuleStationNo);
        }

    }
}
