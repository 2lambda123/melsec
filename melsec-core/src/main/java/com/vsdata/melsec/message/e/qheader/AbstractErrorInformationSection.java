package com.vsdata.melsec.message.e.qheader;

import com.vsdata.melsec.message.Function;

/**
 * @author liumin
 */
public abstract class AbstractErrorInformationSection extends AbstractQHeader implements ErrorInformationSection {

    private Function function;

    private int subcommand;

    @Override
    public Function getFunction() {
        return function;
    }

    @Override
    public void setFunction(Function function) {
        this.function = function;
    }

    @Override
    public int getSubcommand() {
        return subcommand;
    }

    @Override
    public void setSubcommand(int subcommand) {
        this.subcommand = subcommand;
    }
}
