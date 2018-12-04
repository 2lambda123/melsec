package com.vsdata.melsec.message.e;

import com.vsdata.melsec.message.e.qheader.AbstractResponseQHeader;
import com.vsdata.melsec.message.e.qheader.AsciiErrorInformationSection;
import com.vsdata.melsec.message.e.qheader.AsciiResponseQHeader;
import com.vsdata.melsec.message.e.qheader.ErrorInformationSection;
import com.vsdata.melsec.message.e.subheader.Frame3EAsciiResponseSubheader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * @author liumin
 */
public class Frame3EAsciiResponse extends AbstractFrameEResponse {

    private ByteBuf data;

    private ErrorInformationSection errorInformationSection;

    @Override
    protected Subheader newSubheader() {
        return Frame3EAsciiResponseSubheader.getInstance();
    }

    @Override
    protected AbstractResponseQHeader newQHeader() {
        return new AsciiResponseQHeader();
    }

    @Override
    public ByteBuf getData() {
        return data;
    }

    @Override
    public void setData(ByteBuf data) {
        this.data = data;
    }

    @Override
    public ErrorInformationSection getErrorInformationSection() {
        if (errorInformationSection == null) {
            errorInformationSection = new AsciiErrorInformationSection();
        }
        return errorInformationSection;
    }

    @Override
    public String toString() {
        return "Frame3EAsciiResponse{" +
            "qHeader=" + getQHeader() +
            ", data=" + (getData() != null ? ByteBufUtil.hexDump(getData()) : null) +
            ", errorInformationSection=" + errorInformationSection +
            '}';
    }
}
