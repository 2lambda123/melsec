package com.vsdata.melsec.server;

import com.vsdata.melsec.message.Device;
import com.vsdata.melsec.message.Function;
import com.vsdata.melsec.message.e.*;
import com.vsdata.melsec.message.e.qheader.ErrorInformationSection;
import com.vsdata.melsec.utils.BinaryConverters;
import com.vsdata.melsec.utils.ByteBufUtilities;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liumin
 */
public class ServerFrameEMessageHandler extends SimpleChannelInboundHandler {

    private static final Logger log = LoggerFactory.getLogger(ServerFrameEMessageHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FrameECommand) {
            FrameECommand command = (FrameECommand) msg;
            Function function = command.getPrincipal().getFunction();
            int subcommand = command.getPrincipal().getSubcommand();
            Device device = command.getPrincipal().getDevice();
            String realAddress = command.getPrincipal().getRealAddress();
            int points = command.getPrincipal().getPoints();

            boolean isAscii = command instanceof Frame3EAsciiCommand;
            int addr = Integer.parseUnsignedInt(realAddress, device.getRadix().getValue());
            FrameEResponse response;
            if (isAscii) {
                response = new Frame3EAsciiResponse();
            } else {
                response = new Frame3EBinaryResponse();
            }

            if ((addr + points - 1) < device.getLength() && addr > 0 && points > 0) {
                if (function == Function.BATCH_READ) {
                    ByteBuf buffer;
                    byte[] out = new byte[points];
                    if (device.getData() instanceof byte[]) {
                        byte[] source = (byte[]) device.getData();
                        System.arraycopy(source, addr, out, 0, points);
                        buffer = Unpooled.wrappedBuffer(BinaryConverters.convertBoolArrayToBinaryOnBit(out));
                    } else {
                        buffer = Unpooled.buffer();
                        for (int i = addr; i < addr + points; i++) {
                            int[] source = (int[]) device.getData();
                            if (isAscii) {
                                buffer.writeShort(source[i]);
                            } else {
                                buffer.writeShortLE(source[i]);
                            }
                        }
                    }
                    response.setData(buffer);
                } else {
                    ByteBuf buffer = command.getPrincipal().getData();
                    if (device.getData() instanceof byte[]) {
                        byte[] dest = (byte[]) device.getData();
                        byte[] in = ByteBufUtilities.readAllBytes(buffer);
                        if (in != null) {
                            System.arraycopy(in, 0, dest, addr, points);
                        }
                    } else {
                        int bufferIndex = 0;
                        for (int i = addr; i < addr + points; i++) {
                            int[] dest = (int[]) device.getData();
//                            if (isAscii) {
//                                dest[i] = buffer.getUnsignedShort(bufferIndex);
//                            } else {
//                                dest[i] = buffer.getUnsignedShortLE(bufferIndex);
//                            }
                            dest[i] = buffer.getUnsignedShort(bufferIndex);
                            bufferIndex += 2;
                        }
                    }
                }
            } else {
                response.getQHeader().setCompleteCode(0xC051);
                ErrorInformationSection errorInformationSection = response.getErrorInformationSection();
                errorInformationSection.setFunction(function);
                errorInformationSection.setSubcommand(subcommand);
            }

            ctx.pipeline().writeAndFlush(response);
        } else {
            log.error("Unknown type data: " + msg.toString());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Master/client channel closed: {}", ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception caught on channel: {}", ctx.channel(), cause);
        ctx.close();
    }
}

