package com.vsdata.melsec.codec;

import com.vsdata.melsec.message.e.Frame3EBinaryCommand;
import com.vsdata.melsec.message.e.FrameECommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author liumin
 */
public class ServerFrame3EBinaryMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        FrameECommand command = new Frame3EBinaryCommand();
        command.getSubheader().decode(in);
        command.getQHeader().decode(in);
        command.getPrincipal().decode(in);
        out.add(command);
    }
}
