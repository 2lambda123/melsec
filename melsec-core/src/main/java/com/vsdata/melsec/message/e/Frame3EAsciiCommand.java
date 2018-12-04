package com.vsdata.melsec.message.e;

import com.vsdata.melsec.MelsecClientOptions;
import com.vsdata.melsec.message.AsciiPrincipal;
import com.vsdata.melsec.message.Function;
import com.vsdata.melsec.message.Principal;
import com.vsdata.melsec.message.e.qheader.AsciiCommandQHeader;
import com.vsdata.melsec.message.e.subheader.Frame3EAsciiCommandSubheader;
import io.netty.buffer.ByteBuf;

/**
 * @author liumin
 */
public class Frame3EAsciiCommand extends AbstractFrameECommand {

    public Frame3EAsciiCommand() {
    }

    public Frame3EAsciiCommand(Function function, String address, int points) {
        super(function, address, points);
    }

    public Frame3EAsciiCommand(Function function, String address, int points, MelsecClientOptions options) {
        super(function, address, points, options);
    }

    public Frame3EAsciiCommand(Function function, String address, int points, ByteBuf data) {
        super(function, address, points, data);
    }

    public Frame3EAsciiCommand(Function function, String address, int points, ByteBuf data, MelsecClientOptions options) {
        super(function, address, points, data, options);
    }

    @Override
    public Principal newPrincipal() {
        return new AsciiPrincipal(getFunction(), getAddress(), getPoints(), getData());
    }

    @Override
    public Subheader newSubheader() {
        return Frame3EAsciiCommandSubheader.getInstance();
    }

    @Override
    public QHeader newQHeader() {
        return new AsciiCommandQHeader();
    }

    @Override
    public String toString() {
        return "Frame3EAsciiCommand{" +
            "qHeader=" + getQHeader() +
            ", principal=" + getPrincipal() +
            '}';
    }
}
