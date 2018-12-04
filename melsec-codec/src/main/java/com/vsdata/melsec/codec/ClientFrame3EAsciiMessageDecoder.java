package com.vsdata.melsec.codec;

import com.vsdata.melsec.message.e.Frame3EAsciiResponse;
import com.vsdata.melsec.message.e.FrameEResponse;
import com.vsdata.melsec.message.e.Subheader;
import com.vsdata.melsec.message.e.qheader.AbstractResponseQHeader;
import com.vsdata.melsec.utils.ByteBufUtilities;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author liumin
 */
public class ClientFrame3EAsciiMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        FrameEResponse response = new Frame3EAsciiResponse();
        Subheader subheader = response.getSubheader();
        subheader.decode(in);
        AbstractResponseQHeader qHeader = response.getQHeader();
        qHeader.decode(in);
        if (qHeader.getCompleteCode() == 0) {
            response.setData(ByteBufUtilities.readAsciiBuf(in));
        } else {
            response.getErrorInformationSection().decode(in);
        }
        out.add(response);
    }
}
