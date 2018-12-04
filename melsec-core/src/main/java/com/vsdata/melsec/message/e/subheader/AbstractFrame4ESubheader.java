package com.vsdata.melsec.message.e.subheader;

import com.vsdata.melsec.message.e.Subheader;

/**
 * @author liumin
 */
public abstract class AbstractFrame4ESubheader implements Subheader {

    private int serialNo;

    public AbstractFrame4ESubheader() {
    }

    public AbstractFrame4ESubheader(int serialNo) {
        this.serialNo = serialNo;
    }

    /**
     * 获取报文前段
     *
     * @return 报文前段
     */
    protected abstract byte[] getFrontCodes();

    /**
     * 获取报文后段
     *
     * @return 报文后段
     */
    protected abstract byte[] getEndCodes();

    /**
     * 获取串行编号
     *
     * @return 串行编号
     */
    public int getSerialNo() {
        return serialNo;
    }

    /**
     * 设置串行编号
     */
    protected void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }
}
