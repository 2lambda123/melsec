package com.vsdata.melsec.message;

import com.vsdata.melsec.UnknownDeviceException;
import com.vsdata.melsec.utils.BinaryConverters;
import com.vsdata.melsec.utils.ByteBufUtilities;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * @author liumin
 */
public class AsciiPrincipal extends AbstractPrincipal {

    public AsciiPrincipal() {
    }

    public AsciiPrincipal(Function function, String address, int points) {
        super(function, address, points);
    }

    public AsciiPrincipal(Function function, String address, int points, ByteBuf data) {
        super(function, address, points, data);
    }

    @Override
    public void encode0(ByteBuf buf) {
        ByteBufUtilities.writeShortAscii(buf, getFunction().getCommand());
        ByteBufUtilities.writeShortAscii(buf, getSubcommand());
        ByteBufUtil.writeAscii(buf, getDevice().getAsciiCode());
        ByteBufUtil.writeAscii(buf, ("000000" + getRealAddress()).substring(getRealAddress().length()));
        ByteBufUtilities.writeShortAscii(buf, getPoints());
        if (getData() != null) {
            if (getDevice().getType() == UnitType.BIT) {
                byte[] bytes = ByteBufUtilities.readAllBytes(getData());
                if (bytes != null) {
                    buf.writeBytes(BinaryConverters.convertBoolArrayToAsciiOnBit(bytes));
                }
            } else {
                ByteBufUtilities.writeAsciiBuf(buf, getData());
            }
        }
    }

    @Override
    public boolean decode(ByteBuf buf) throws Exception {
        setFunction(Function.from(ByteBufUtilities.readShortAscii(buf)));
        setSubcommand(ByteBufUtilities.readShortAscii(buf));
        String asciiCode = buf.readCharSequence(2, StandardCharsets.US_ASCII).toString();
        Device device = Device.fromAsciiCode(asciiCode);
        if (device == null) {
            throw new UnknownDeviceException(asciiCode);
        }
        setDevice(device);
        String realAddress = buf.readCharSequence(6, StandardCharsets.US_ASCII).toString();
        // 去掉前置的零
        int index = 0;
        for (int i = 0; i < realAddress.length(); i++) {
            if ('0' != realAddress.charAt(i)) {
                index = i;
                break;
            }
        }
        realAddress = realAddress.substring(index);
        setRealAddress(realAddress);
        int points = ByteBufUtilities.readShortAscii(buf);
        setPoints(points);

        int remaining = buf.readableBytes();
        if (remaining > 0) {
            if (device.getType() == UnitType.BIT) {
                byte[] data = new byte[remaining];
                buf.readBytes(data);
                setData(Unpooled.wrappedBuffer(BinaryConverters.convertAsciiOnBitToBoolArray(data, points)));
            } else {
                setData(ByteBufUtilities.readAsciiBuf(buf));
            }
        }

        setAddress(device.toString() + realAddress);
        return true;
    }

    @Override
    public String toString() {
        return "AsciiPrincipal{" +
            "address='" + getAddress() + '\'' +
            ", points=" + getPoints() +
            ", function=" + getFunction() +
            ", subcommand=" + getSubcommand() +
            ", data=" + (getData() != null ? ByteBufUtil.hexDump(getData()) : null) +
            '}';
    }
}
