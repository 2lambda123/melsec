package com.vsdata.melsec.message.e.qheader;

import com.vsdata.melsec.message.Function;
import com.vsdata.melsec.message.e.QHeader;

/**
 * @author liumin
 */
public interface ErrorInformationSection extends QHeader {

    /**
     * 获取动作
     *
     * @return 动作
     */
    Function getFunction();

    /**
     * 设置动作
     *
     * @param function 动作
     */
    void setFunction(Function function);

    /**
     * 获取子指令
     *
     * @return 子指令
     */
    int getSubcommand();

    /**
     * 设置子指令
     *
     * @param subcommand 子指令
     */
    void setSubcommand(int subcommand);
}
