package com.vsdata.melsec.codec;

import com.vsdata.melsec.message.Function;
import com.vsdata.melsec.message.e.FrameEResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author liumin
 */
public class ClientFrameEMessageDecoderTest {

    @Test
    public void test3EAsciiBatchWrite() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EAsciiByteDecoder(),
            new ClientFrame3EAsciiMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
            0x44, 0x30, 0x30, 0x30,
            0x30, 0x30,
            0x46, 0x46,
            0x30, 0x33, 0x46, 0x46,
            0x30, 0x30,
            0x30, 0x30, 0x30, 0x34,
            0x30, 0x30, 0x30, 0x30
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameEResponse response = channel.readInbound();
        assertEquals(0, response.getQHeader().getCompleteCode());
    }

    @Test
    public void test3EAsciiBatchReadBool() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EAsciiByteDecoder(),
            new ClientFrame3EAsciiMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
            0x44, 0x30, 0x30, 0x30,
            0x30, 0x30,
            0x46, 0x46,
            0x30, 0x33, 0x46, 0x46,
            0x30, 0x30,
            0x30, 0x30, 0x30, 0x43,
            0x30, 0x30, 0x30, 0x30,
            0x30, 0x30, 0x30, 0x31, 0x30, 0x30, 0x31, 0x31
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameEResponse response = channel.readInbound();
        assertEquals(0, response.getQHeader().getCompleteCode());
        assertEquals(Unpooled.wrappedBuffer(new byte[]{
            0x00, 0x01, 0x00, 0x11
        }), response.getData());
    }

    @Test
    public void test3EBinaryBatchReadBool() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EBinaryByteDecoder(),
            new ClientFrame3EBinaryMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
            (byte) 0xD0, 0x00,
            0x00,
            (byte) 0xFF,
            (byte) 0xFF, 0x03,
            0x00,
            0x06, 0x00,
            0x00, 0x00,
            0x00, 0x01, 0x00, 0x11
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameEResponse response = channel.readInbound();
        assertEquals(0, response.getQHeader().getCompleteCode());
        assertEquals(Unpooled.wrappedBuffer(new byte[]{
            0x00, 0x01, 0x00, 0x11
        }), response.getData());
    }

    @Test
    public void test3EAsciiBatchCommandError() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EAsciiByteDecoder(),
            new ClientFrame3EAsciiMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
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
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameEResponse response = channel.readInbound();
        assertEquals(0xC051, response.getQHeader().getCompleteCode());

        assertEquals(Function.BATCH_READ, response.getErrorInformationSection().getFunction());
    }

    @Test
    public void test3EBinaryBatchCommandError() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EBinaryByteDecoder(),
            new ClientFrame3EBinaryMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
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
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameEResponse response = channel.readInbound();
        assertEquals(0xC051, response.getQHeader().getCompleteCode());

        assertEquals(0xFF, response.getErrorInformationSection().getPcNo());
        assertEquals(Function.BATCH_READ, response.getErrorInformationSection().getFunction());
    }

    @Test
    public void test3EAsciiBatchReadShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EAsciiByteDecoder(),
            new ClientFrame3EAsciiMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
            0x44, 0x30, 0x30, 0x30,
            0x30, 0x30,
            0x46, 0x46,
            0x30, 0x33, 0x46, 0x46,
            0x30, 0x30,
            0x30, 0x30, 0x31, 0x30,
            0x30, 0x30, 0x30, 0x30,
            0x31, 0x32, 0x33, 0x34, 0x30, 0x30, 0x30, 0x32, 0x43, 0x44, 0x45, 0x46
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameEResponse response = channel.readInbound();
        assertEquals(0, response.getQHeader().getCompleteCode());
        assertEquals(Unpooled.wrappedBuffer(new byte[]{
            0x12, 0x34, 0x00, 0x02, (byte) 0xCD, (byte) 0xEF
        }), response.getData());
    }

    @Test
    public void test3EBinaryBatchReadShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EBinaryByteDecoder(),
            new ClientFrame3EBinaryMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
            (byte) 0xD0, 0x00,
            0x00,
            (byte) 0xFF,
            (byte) 0xFF, 0x03,
            0x00,
            0x08, 0x00,
            0x00, 0x00,
            0x34, 0x12, 0x02, 0x00, (byte) 0xEF, (byte) 0xCD
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameEResponse response = channel.readInbound();
        assertEquals(0, response.getQHeader().getCompleteCode());
        assertEquals(Unpooled.wrappedBuffer(new byte[]{
            0x34, 0x12, 0x02, 0x00, (byte) 0xEF, (byte) 0xCD
        }), response.getData());
    }
}
