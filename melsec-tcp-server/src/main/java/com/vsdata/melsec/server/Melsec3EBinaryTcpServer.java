package com.vsdata.melsec.server;

import com.vsdata.melsec.codec.Frame3EBinaryByteDecoder;
import com.vsdata.melsec.codec.ServerFrame3EBinaryMessageDecoder;
import com.vsdata.melsec.codec.ServerFrameEMessageEncoder;
import io.netty.channel.ChannelPipeline;

/**
 * @author liumin
 */
public class Melsec3EBinaryTcpServer extends AbstractMelsecTcpServer {

    public Melsec3EBinaryTcpServer(MelsecTcpServerConfig config) {
        super(config);
    }

    @Override
    protected void initChannel(ChannelPipeline pipeline) {
        pipeline.addLast(ServerFrameEMessageEncoder.INSTANCE);
        pipeline.addLast(new Frame3EBinaryByteDecoder());
        pipeline.addLast(new ServerFrame3EBinaryMessageDecoder());
    }
}
