package com.vsdata.melsec.codec;

import com.vsdata.melsec.message.e.FrameECommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author liumin
 */
public class ServerFrameEMessageDecoderTest {

    @Test
    public void test3EReadAsciiBool() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EAsciiByteDecoder(),
            new ServerFrame3EAsciiMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
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
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameECommand command = channel.readInbound();
        assertEquals("M*", command.getPrincipal().getDevice().getAsciiCode());
        assertEquals("100", command.getPrincipal().getRealAddress());
        assertEquals("M100", command.getPrincipal().getAddress());
        assertEquals(8, command.getPrincipal().getPoints());
    }

    @Test
    public void test3EWriteAsciiBool() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EAsciiByteDecoder(),
            new ServerFrame3EAsciiMessageDecoder());

        ByteBuf byteBuf = Unpooled.wrappedBuffer(new byte[]{
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
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameECommand command = channel.readInbound();
        assertEquals("M*", command.getPrincipal().getDevice().getAsciiCode());
        assertEquals("100", command.getPrincipal().getRealAddress());
        assertEquals("M100", command.getPrincipal().getAddress());
        assertEquals(8, command.getPrincipal().getPoints());

        assertEquals(Unpooled.wrappedBuffer(new byte[]{
            0x01, 0x01, 0x00, 0x00, 0x01, 0x01, 0x00, 0x00
        }), command.getPrincipal().getData());
    }

    @Test
    public void test3EReadBinaryBool() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EBinaryByteDecoder(),
            new ServerFrame3EBinaryMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
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
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameECommand command = channel.readInbound();
        assertEquals("M*", command.getPrincipal().getDevice().getAsciiCode());
        assertEquals("100", command.getPrincipal().getRealAddress());
        assertEquals("M100", command.getPrincipal().getAddress());
        assertEquals(8, command.getPrincipal().getPoints());
    }

    @Test
    public void test3EWriteBinaryBool() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EBinaryByteDecoder(),
            new ServerFrame3EBinaryMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
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
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameECommand command = channel.readInbound();
        assertEquals("M*", command.getPrincipal().getDevice().getAsciiCode());
        assertEquals("100", command.getPrincipal().getRealAddress());
        assertEquals("M100", command.getPrincipal().getAddress());
        assertEquals(8, command.getPrincipal().getPoints());

        assertEquals(Unpooled.wrappedBuffer(new byte[]{
            0x01, 0x01, 0x00, 0x00, 0x01, 0x01, 0x00, 0x00
        }), command.getPrincipal().getData());
    }

    @Test
    public void test3EReadAsciiShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EAsciiByteDecoder(),
            new ServerFrame3EAsciiMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
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
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameECommand command = channel.readInbound();
        assertEquals("TN", command.getPrincipal().getDevice().getAsciiCode());
        assertEquals("100", command.getPrincipal().getRealAddress());
        assertEquals("T100", command.getPrincipal().getAddress());
        assertEquals(3, command.getPrincipal().getPoints());
    }

    @Test
    public void test3EReadBinaryShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EBinaryByteDecoder(),
            new ServerFrame3EBinaryMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
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
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameECommand command = channel.readInbound();
        assertEquals("TN", command.getPrincipal().getDevice().getAsciiCode());
        assertEquals("100", command.getPrincipal().getRealAddress());
        assertEquals("T100", command.getPrincipal().getAddress());
        assertEquals(3, command.getPrincipal().getPoints());
    }

    @Test
    public void test3EWriteAsciiShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EAsciiByteDecoder(),
            new ServerFrame3EAsciiMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
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
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameECommand command = channel.readInbound();
        assertEquals("D*", command.getPrincipal().getDevice().getAsciiCode());
        assertEquals("100", command.getPrincipal().getRealAddress());
        assertEquals("D100", command.getPrincipal().getAddress());
        assertEquals(3, command.getPrincipal().getPoints());
        assertEquals(Unpooled.buffer(6)
            .writeShort(6549)
            .writeShort(4610)
            .writeShort(4400), command.getPrincipal().getData());
    }

    @Test
    public void test3EWriteBinaryShort() {
        EmbeddedChannel channel = new EmbeddedChannel(new Frame3EBinaryByteDecoder(),
            new ServerFrame3EBinaryMessageDecoder());

        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{
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
        });

        assertTrue(channel.writeInbound(byteBuf));
        assertTrue(channel.finish());

        FrameECommand command = channel.readInbound();
        assertEquals("D*", command.getPrincipal().getDevice().getAsciiCode());
        assertEquals("100", command.getPrincipal().getRealAddress());
        assertEquals("D100", command.getPrincipal().getAddress());
        assertEquals(3, command.getPrincipal().getPoints());
        assertEquals(Unpooled.buffer()
            .writeShort(6549)
            .writeShort(4610)
            .writeShort(4400), command.getPrincipal().getData());
    }
}
