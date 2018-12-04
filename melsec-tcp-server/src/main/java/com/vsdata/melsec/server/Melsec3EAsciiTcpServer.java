package com.vsdata.melsec.server;

import com.vsdata.melsec.codec.Frame3EAsciiByteDecoder;
import com.vsdata.melsec.codec.ServerFrame3EAsciiMessageDecoder;
import com.vsdata.melsec.codec.ServerFrameEMessageEncoder;
import io.netty.channel.ChannelPipeline;

/**
 * @author liumin
 */
public class Melsec3EAsciiTcpServer extends AbstractMelsecTcpServer {

    public Melsec3EAsciiTcpServer(MelsecTcpServerConfig config) {
        super(config);
    }

    @Override
    protected void initChannel(ChannelPipeline pipeline) {
        pipeline.addLast(ServerFrameEMessageEncoder.INSTANCE);
        pipeline.addLast(new Frame3EAsciiByteDecoder());
        pipeline.addLast(new ServerFrame3EAsciiMessageDecoder());
    }
}
