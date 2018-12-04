package com.vsdata.melsec.codec;

import com.vsdata.melsec.message.e.Frame3EBinaryResponse;
import com.vsdata.melsec.message.e.FrameEResponse;
import com.vsdata.melsec.message.e.Subheader;
import com.vsdata.melsec.message.e.qheader.AbstractResponseQHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author liumin
 */
public class ClientFrame3EBinaryMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        FrameEResponse response = new Frame3EBinaryResponse();
        Subheader subheader = response.getSubheader();
        subheader.decode(in);
        AbstractResponseQHeader qHeader = response.getQHeader();
        qHeader.decode(in);
        if (qHeader.getCompleteCode() == 0) {
            int remaining = in.readableBytes();
            if (remaining > 0) {
                byte[] bytes = new byte[remaining];
                in.readBytes(bytes);
                response.setData(Unpooled.wrappedBuffer(bytes));
            }
        } else {
            response.getErrorInformationSection().decode(in);
        }
        out.add(response);
    }
}
