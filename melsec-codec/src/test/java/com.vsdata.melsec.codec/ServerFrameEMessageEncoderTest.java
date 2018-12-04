package com.vsdata.melsec.codec;

import com.vsdata.melsec.message.Function;
import com.vsdata.melsec.message.e.Frame3EAsciiResponse;
import com.vsdata.melsec.message.e.Frame3EBinaryResponse;
import com.vsdata.melsec.message.e.FrameEResponse;
import com.vsdata.melsec.message.e.qheader.ErrorInformationSection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author liumin
 */
public class ServerFrameEMessageEncoderTest {

    @Test
    public void test3EAsciiBatchWrite() {
        EmbeddedChannel channel = new EmbeddedChannel(new ServerFrameEMessageEncoder());

        FrameEResponse response = new Frame3EAsciiResponse();

        assertTrue(channel.writeOutbound(response));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] data = new byte[size];
        out.readBytes(data);

        assertArrayEquals(new byte[]{
            0x44, 0x30, 0x30, 0x30,
            0x30, 0x30,
            0x46, 0x46,
            0x30, 0x33, 0x46, 0x46,
            0x30, 0x30,
            0x30, 0x30, 0x30, 0x34,
            0x30, 0x30, 0x30, 0x30
        }, data);
    }

    @Test
    public void test3EAsciiBatchReadBool() {
        EmbeddedChannel channel = new EmbeddedChannel(new ServerFrameEMessageEncoder());

        FrameEResponse response = new Frame3EAsciiResponse();
        response.setData(Unpooled.wrappedBuffer(new byte[]{
            0x00, 0x01, 0x00, 0x11
        }));

        assertTrue(channel.writeOutbound(response));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] data = new byte[size];
        out.readBytes(data);

        assertArrayEquals(new byte[]{
            0x44, 0x30, 0x30, 0x30,
            0x30, 0x30,
            0x46, 0x46,
            0x30, 0x33, 0x46, 0x46,
            0x30, 0x30,
            0x30, 0x30, 0x30, 0x43,
            0x30, 0x30, 0x30, 0x30,
            0x30, 0x30, 0x30, 0x31, 0x30, 0x30, 0x31, 0x31
        }, data);
    }

    @Test
    public void test3EBinaryBatchReadBool() {
        EmbeddedChannel channel = new EmbeddedChannel(new ServerFrameEMessageEncoder());

        FrameEResponse response = new Frame3EBinaryResponse();
        response.setData(Unpooled.wrappedBuffer(new byte[]{
            0x00, 0x01, 0x00, 0x11
        }));

        assertTrue(channel.writeOutbound(response));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] data = new byte[size];
        out.readBytes(data);

        assertArrayEquals(new byte[]{
            (byte) 0xD0, 0x00,
            0x00,
            (byte) 0xFF,
            (byte) 0xFF, 0x03,
            0x00,
            0x06, 0x00,
            0x00, 0x00,
            0x00, 0x01, 0x00, 0x11
        }, data);
    }

    @Test
    public void test3EAsciiBatchCommandError() {
        EmbeddedChannel channel = new EmbeddedChannel(new ServerFrameEMessageEncoder());

        FrameEResponse response = new Frame3EAsciiResponse();
        response.getQHeader().setCompleteCode(0xC051);
        ErrorInformationSection errorInformationSection = response.getErrorInformationSection();
        errorInformationSection.setFunction(Function.BATCH_READ);
        errorInformationSection.setSubcommand(0x0001);

        assertTrue(channel.writeOutbound(response));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] data = new byte[size];
        out.readBytes(data);

        assertArrayEquals(new byte[]{
            0x44, 0x30, 0x30, 0x30,
            0x30, 0x30,
            0x46, 0x46,
            0x30, 0x33, 0x46, 0x46,
            0x30, 0x30,
            0x30, 0x30, 0x31, 0x36,
            0x43, 0x30, 0x35, 0x31,
            0x30, 0x30,
            0x46, 0x46,
            0x30, 0x33, 0x46, 0x46,
            0x30, 0x30,
            0x30, 0x34, 0x30, 0x31,
            0x30, 0x30, 0x30, 0x31
        }, data);
    }

    @Test
    public void test3EBinaryBatchCommandError() {
        EmbeddedChannel channel = new EmbeddedChannel(new ServerFrameEMessageEncoder());

        FrameEResponse response = new Frame3EBinaryResponse();
        response.getQHeader().setCompleteCode(0xC051);
        ErrorInformationSection errorInformationSection = response.getErrorInformationSection();
        errorInformationSection.setFunction(Function.BATCH_READ);
        errorInformationSection.setSubcommand(0x0001);

        assertTrue(channel.writeOutbound(response));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] data = new byte[size];
        out.readBytes(data);

        assertArrayEquals(new byte[]{
            (byte) 0xD0, 0x00,
            0x00,
            (byte) 0xFF,
            (byte) 0xFF, 0x03,
            0x00,
            0x0B, 0x00,
            0x51, (byte) 0xC0,
            0x00,
            (byte) 0xFF,
            (byte) 0xFF, 0x03,
            0x00,
            0x01, 0x04,
            0x01, 0x00
        }, data);
    }

    @Test
    public void test3EAsciiBatchReadShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new ServerFrameEMessageEncoder());

        FrameEResponse response = new Frame3EAsciiResponse();
        response.setData(Unpooled.wrappedBuffer(new byte[]{
            0x12, 0x34, 0x00, 0x02, (byte) 0xCD, (byte) 0xEF
        }));

        assertTrue(channel.writeOutbound(response));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] data = new byte[size];
        out.readBytes(data);

        assertArrayEquals(new byte[]{
            0x44, 0x30, 0x30, 0x30,
            0x30, 0x30,
            0x46, 0x46,
            0x30, 0x33, 0x46, 0x46,
            0x30, 0x30,
            0x30, 0x30, 0x31, 0x30,
            0x30, 0x30, 0x30, 0x30,
            0x31, 0x32, 0x33, 0x34, 0x30, 0x30, 0x30, 0x32, 0x43, 0x44, 0x45, 0x46
        }, data);
    }

    @Test
    public void test3EBinaryBatchReadShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new ServerFrameEMessageEncoder());

        FrameEResponse response = new Frame3EBinaryResponse();
        response.setData(Unpooled.wrappedBuffer(new byte[]{
            0x34, 0x12, 0x02, 0x00, (byte) 0xEF, (byte) 0xCD
        }));

        assertTrue(channel.writeOutbound(response));
        assertTrue(channel.finish());

        ByteBuf out = channel.readOutbound();
        int size = out.readableBytes();
        byte[] data = new byte[size];
        out.readBytes(data);

        assertArrayEquals(new byte[]{
            (byte) 0xD0, 0x00,
            0x00,
            (byte) 0xFF,
            (byte) 0xFF, 0x03,
            0x00,
            0x08, 0x00,
            0x00, 0x00,
            0x34, 0x12, 0x02, 0x00, (byte) 0xEF, (byte) 0xCD
        }, data);
    }
}
