package com.vsdata.melsec.example.client;

import com.vsdata.melsec.client.MelsecClientConfig;
import com.vsdata.melsec.client.MelsecTcpClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author liumin
 */
public class Melsec3EBinaryClientExample {

    public static void main(String[] args) {
        MelsecClientConfig config = new MelsecClientConfig.Builder("localhost").build();
        MelsecTcpClient client = MelsecTcpClient.create3EBinary(config);

        ByteBuf data = Unpooled.buffer(8);
        data.writeBoolean(false);
        data.writeBoolean(true);
        data.writeBoolean(false);
        data.writeBoolean(false);
        data.writeBoolean(true);
        data.writeBoolean(false);
        data.writeBoolean(true);
        data.writeBoolean(true);

//        client.batchWrite("M100", 8, data)
//            .thenCompose(r -> client.batchRead("M100", 8))
//            .thenAccept(response -> {
//                System.out.println(ByteBufUtil.hexDump(response));
//            })
//            .exceptionally(ex -> {
//                ex.printStackTrace();
//                return null;
//            });

//        ByteBuf data1 = Unpooled.buffer(6);
//        data1.writeShort(6549);
//        data1.writeShort(4610);
//        data1.writeShort(4400);
//
//        client.batchWrite("D100", 3, data1)
//            .thenCompose(r -> client.batchRead("D100", 3))
//            .thenAccept(response -> {
//                System.out.println("D100: " + response.readShort());
//                System.out.println("D101: " + response.readShort());
//                System.out.println("D102: " + response.readShort());
//            })
//            .exceptionally(ex -> {
//                ex.printStackTrace();
//                return null;
//            });

        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            client.batchRead("D100", 3)
                .thenAccept(response -> {
                    System.out.println(ByteBufUtil.hexDump(response));
                    System.out.println("D100: " + response.readShort());
                    System.out.println("D101: " + response.readShort());
                    System.out.println("D102: " + response.readShort());
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
            client.batchRead("M100", 3)
                .thenAccept(response -> {
                    System.out.println(ByteBufUtil.hexDump(response));
                    System.out.println("M100: " + response.readBoolean());
                    System.out.println("M101: " + response.readBoolean());
                    System.out.println("M102: " + response.readBoolean());
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
        }, 1000, 1000, TimeUnit.MILLISECONDS);

        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            client.batchRead("M200", 2)
                .thenAccept(response -> {
                    System.out.println("M200: " + response.readBoolean());
                    System.out.println("M201: " + response.readBoolean());
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
            client.batchRead("D200", 2)
                .thenAccept(response -> {
                    System.out.println("D200: " + response.readShort());
                    System.out.println("D201: " + response.readShort());
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
        }, 800, 800, TimeUnit.MILLISECONDS);
    }
}
