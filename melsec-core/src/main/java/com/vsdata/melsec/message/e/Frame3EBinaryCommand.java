package com.vsdata.melsec.message.e;

import com.vsdata.melsec.MelsecClientOptions;
import com.vsdata.melsec.message.BinaryPrincipal;
import com.vsdata.melsec.message.Function;
import com.vsdata.melsec.message.Principal;
import com.vsdata.melsec.message.e.qheader.BinaryCommandQHeader;
import com.vsdata.melsec.message.e.subheader.Frame3EBinaryCommandSubheader;
import io.netty.buffer.ByteBuf;

/**
 * @author liumin
 */
public class Frame3EBinaryCommand extends AbstractFrameECommand {

    public Frame3EBinaryCommand() {
    }

    public Frame3EBinaryCommand(Function function, String address, int points) {
        super(function, address, points);
    }

    public Frame3EBinaryCommand(Function function, String address, int points, ByteBuf data) {
        super(function, address, points, data);
    }

    public Frame3EBinaryCommand(Function function, String address, int points, MelsecClientOptions options) {
        super(function, address, points, options);
    }

    public Frame3EBinaryCommand(Function function, String address, int points, ByteBuf data, MelsecClientOptions options) {
        super(function, address, points, data, options);
    }

    @Override
    public Principal newPrincipal() {
        return new BinaryPrincipal(getFunction(), getAddress(), getPoints(), getData());
    }

    @Override
    public Subheader newSubheader() {
        return Frame3EBinaryCommandSubheader.getInstance();
    }

    @Override
    public QHeader newQHeader() {
        return new BinaryCommandQHeader();
    }

    @Override
    public String toString() {
        return "Frame3EBinaryCommand{" +
            "qHeader=" + getQHeader() +
            ", principal=" + getPrincipal() +
            '}';
    }
}
