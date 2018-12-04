package com.vsdata.melsec.codec;

import com.vsdata.melsec.message.Function;
import com.vsdata.melsec.message.e.Frame3EAsciiCommand;
import com.vsdata.melsec.message.e.Frame3EBinaryCommand;
import com.vsdata.melsec.message.e.FrameECommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author liumin
 */
public class ClientFrameEMessageEncoderTest {

    @Test
    public void test3EReadAsciiBool() {
        EmbeddedChannel channel = new EmbeddedChannel(new ClientFrameEMessageEncoder());

        FrameECommand message = new Frame3EAsciiCommand(Function.BATCH_READ, "M100", 8);

        assertTrue(channel.writeOutbound(message));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] data = new byte[size];
        out.readBytes(data);

        assertArrayEquals(new byte[]{
            0x35, 0x30, 0x30, 0x30,
            0x30, 0x30,
            0x46, 0x46,
            0x30, 0x33, 0x46, 0x46,
            0x30, 0x30,
            0x30, 0x30, 0x31, 0x38,
            0x30, 0x30, 0x31, 0x30,

            0x30, 0x34, 0x30, 0x31,
            0x30, 0x30, 0x30, 0x31,
            0x4D, 0x2A,
            0x30, 0x30, 0x30, 0x31, 0x30, 0x30,
            0x30, 0x30, 0x30, 0x38
        }, data);
    }

    @Test
    public void test3EWriteAsciiBool() {
        EmbeddedChannel channel = new EmbeddedChannel(new ClientFrameEMessageEncoder());

        ByteBuf data = Unpooled.buffer(8);
        data.writeBoolean(true);
        data.writeBoolean(true);
        data.writeBoolean(false);
        data.writeBoolean(false);
        data.writeBoolean(true);
        data.writeBoolean(true);
        data.writeBoolean(false);
        data.writeBoolean(false);
        FrameECommand message = new Frame3EAsciiCommand(Function.BATCH_WRITE, "M100", 8, data);

        assertTrue(channel.writeOutbound(message));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] result = new byte[size];
        out.readBytes(result);

        assertArrayEquals(new byte[]{
            0x35, 0x30, 0x30, 0x30,
            0x30, 0x30,
            0x46, 0x46,
            0x30, 0x33, 0x46, 0x46,
            0x30, 0x30,
            0x30, 0x30, 0x32, 0x30,
            0x30, 0x30, 0x31, 0x30,

            0x31, 0x34, 0x30, 0x31,
            0x30, 0x30, 0x30, 0x31,
            0x4D, 0x2A,
            0x30, 0x30, 0x30, 0x31, 0x30, 0x30,
            0x30, 0x30, 0x30, 0x38,
            0x31, 0x31, 0x30, 0x30, 0x31, 0x31, 0x30, 0x30
        }, result);
    }

    @Test
    public void test3EReadBinaryBool() {
        EmbeddedChannel channel = new EmbeddedChannel(new ClientFrameEMessageEncoder());

        FrameECommand message = new Frame3EBinaryCommand(Function.BATCH_READ, "M100", 8);

        assertTrue(channel.writeOutbound(message));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] data = new byte[size];
        out.readBytes(data);

        assertArrayEquals(new byte[]{
            0x50, 0x00,
            0x00,
            (byte) 0xFF,
            (byte) 0xFF, 0x03,
            0x00,
            0x0C, 0x00,
            0x10, 0x00,

            0x01, 0x04,
            0x01, 0x00,
            0x64, 0x00, 0x00,
            (byte) 0x90,
            0x08, 0x00
        }, data);
    }

    @Test
    public void test3EWriteBinaryBool() {
        EmbeddedChannel channel = new EmbeddedChannel(new ClientFrameEMessageEncoder());

        ByteBuf data = Unpooled.buffer(8);
        data.writeBoolean(true);
        data.writeBoolean(true);
        data.writeBoolean(false);
        data.writeBoolean(false);
        data.writeBoolean(true);
        data.writeBoolean(true);
        data.writeBoolean(false);
        data.writeBoolean(false);
        FrameECommand message = new Frame3EBinaryCommand(Function.BATCH_WRITE, "M100", 8, data);

        assertTrue(channel.writeOutbound(message));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] result = new byte[size];
        out.readBytes(result);

        assertArrayEquals(new byte[]{
            0x50, 0x00,
            0x00,
            (byte) 0xFF,
            (byte) 0xFF, 0x03,
            0x00,
            0x10, 0x00,
            0x10, 0x00,

            0x01, 0x14,
            0x01, 0x00,
            0x64, 0x00, 0x00,
            (byte) 0x90,
            0x08, 0x00,
            0x11, 0x00, 0x11, 0x00
        }, result);
    }

    @Test
    public void test3EReadAsciiShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new ClientFrameEMessageEncoder());

        FrameECommand message = new Frame3EAsciiCommand(Function.BATCH_READ, "T100", 3);

        assertTrue(channel.writeOutbound(message));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] data = new byte[size];
        out.readBytes(data);

        assertArrayEquals(new byte[]{
            0x35, 0x30, 0x30, 0x30,
            0x30, 0x30,
            0x46, 0x46,
            0x30, 0x33, 0x46, 0x46,
            0x30, 0x30,
            0x30, 0x30, 0x31, 0x38,
            0x30, 0x30, 0x31, 0x30,

            0x30, 0x34, 0x30, 0x31,
            0x30, 0x30, 0x30, 0x30,
            0x54, 0x4E,
            0x30, 0x30, 0x30, 0x31, 0x30, 0x30,
            0x30, 0x30, 0x30, 0x33
        }, data);
    }

    @Test
    public void test3EReadBinaryShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new ClientFrameEMessageEncoder());

        FrameECommand message = new Frame3EBinaryCommand(Function.BATCH_READ, "T100", 3);

        assertTrue(channel.writeOutbound(message));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] data = new byte[size];
        out.readBytes(data);

        assertArrayEquals(new byte[]{
            0x50, 0x00,
            0x00,
            (byte) 0xFF,
            (byte) 0xFF, 0x03,
            0x00,
            0x0C, 0x00,
            0x10, 0x00,

            0x01, 0x04,
            0x00, 0x00,
            0x64, 0x00, 0x00,
            (byte) 0xC2,
            0x03, 0x00
        }, data);
    }

    @Test
    public void test3EWriteAsciiShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new ClientFrameEMessageEncoder());

        FrameECommand message = new Frame3EAsciiCommand(Function.BATCH_WRITE, "D100", 3,
            Unpooled.buffer()
                .writeShort(6549)
                .writeShort(4610)
                .writeShort(4400));

        assertTrue(channel.writeOutbound(message));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] data = new byte[size];
        out.readBytes(data);

        assertArrayEquals(new byte[]{
            0x35, 0x30, 0x30, 0x30,
            0x30, 0x30,
            0x46, 0x46,
            0x30, 0x33, 0x46, 0x46,
            0x30, 0x30,
            0x30, 0x30, 0x32, 0x34,
            0x30, 0x30, 0x31, 0x30,

            0x31, 0x34, 0x30, 0x31,
            0x30, 0x30, 0x30, 0x30,
            0x44, 0x2A,
            0x30, 0x30, 0x30, 0x31, 0x30, 0x30,
            0x30, 0x30, 0x30, 0x33,
            0x31, 0x39, 0x39, 0x35, 0x31, 0x32, 0x30, 0x32, 0x31, 0x31, 0x33, 0x30
        }, data);
    }

    @Test
    public void test3EWriteBinaryShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new ClientFrameEMessageEncoder());

        FrameECommand message = new Frame3EBinaryCommand(Function.BATCH_WRITE, "D100", 3,
            Unpooled.buffer()
                .writeShort(6549)
                .writeShort(4610)
                .writeShort(4400));

        assertTrue(channel.writeOutbound(message));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] data = new byte[size];
        out.readBytes(data);

        assertArrayEquals(new byte[]{
            0x50, 0x00,
            0x00,
            (byte) 0xFF,
            (byte) 0xFF, 0x03,
            0x00,
            0x12, 0x00,
            0x10, 0x00,

            0x01, 0x14,
            0x00, 0x00,
            0x64, 0x00, 0x00,
            (byte) 0xA8,
            0x03, 0x00,
            (byte) 0x95, 0x19, 0x02, 0x12, 0x30, 0x11
        }, data);
    }
}
