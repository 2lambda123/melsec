package com.vsdata.melsec.example.server;

import com.vsdata.melsec.server.MelsecTcpServer;
import com.vsdata.melsec.server.MelsecTcpServerConfig;

/**
 * @author liumin
 */
public class Melsec3EBinaryTcpServerExample {

    public static void main(String[] args) {
        MelsecTcpServerConfig config = new MelsecTcpServerConfig.Builder().build();
        MelsecTcpServer server = MelsecTcpServer.create3EBinary(config);
        server.bind("localhost", 6700);
    }
}
