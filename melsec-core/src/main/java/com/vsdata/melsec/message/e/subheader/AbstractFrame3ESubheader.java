package com.vsdata.melsec.message.e.subheader;

import com.vsdata.melsec.message.e.Subheader;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * @author liumin
 */
public abstract class AbstractFrame3ESubheader implements Subheader {

    /**
     * 获取报文
     *
     * @return 报文
     */
    protected abstract byte[] getCodes();

    @Override
    public void encode(ByteBuf buf) {
        buf.writeBytes(getCodes());
    }

    @Override
    public boolean decode(ByteBuf buf) {
        byte[] bytes = new byte[getCodes().length];
        buf.readBytes(bytes);
        return Arrays.equals(getCodes(), bytes);
    }
}
