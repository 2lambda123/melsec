package com.vsdata.melsec.codec;

import com.vsdata.melsec.message.e.Frame3EAsciiCommand;
import com.vsdata.melsec.message.e.FrameECommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author liumin
 */
public class ServerFrame3EAsciiMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        FrameECommand command = new Frame3EAsciiCommand();
        command.getSubheader().decode(in);
        command.getQHeader().decode(in);
        command.getPrincipal().decode(in);
        out.add(command);
    }
}
