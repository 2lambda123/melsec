package com.vsdata.melsec.message.e;

import com.vsdata.melsec.message.e.qheader.AbstractResponseQHeader;
import com.vsdata.melsec.message.e.qheader.ErrorInformationSection;
import io.netty.buffer.ByteBuf;

/**
 * @author liumin
 */
public interface FrameEResponse extends FrameEMessage {

    /**
     * 获取Q Header
     *
     * @return Q Header
     */
    @Override
    AbstractResponseQHeader getQHeader();

    /**
     * 设置读取到的数据
     *
     * @param data 数据Buffer
     */
    void setData(ByteBuf data);

    /**
     * 获取读取到的数据
     *
     * @return 数据Buffer
     */
    ByteBuf getData();

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    ErrorInformationSection getErrorInformationSection();
}
