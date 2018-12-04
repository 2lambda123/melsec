package com.vsdata.melsec.example.client;

import com.vsdata.melsec.client.MelsecClientConfig;
import com.vsdata.melsec.client.MelsecTcpClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

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

        client.batchWrite("M100", 8, data)
            .thenCompose(r -> client.batchRead("M100", 8))
            .thenAccept(response -> {
                System.out.println(ByteBufUtil.hexDump(response));
            })
            .exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });

        ByteBuf data1 = Unpooled.buffer(6);
        data1.writeShort(6549);
        data1.writeShort(4610);
        data1.writeShort(4400);

        client.batchWrite("D100", 3, data1)
            .thenCompose(r -> client.batchRead("D100", 3))
            .thenAccept(response -> {
                System.out.println(response.readShort());
                System.out.println(response.readShort());
                System.out.println(response.readShort());
            })
            .exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });
    }
}
