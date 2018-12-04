package com.vsdata.melsec.example.server;

import com.vsdata.melsec.server.MelsecTcpServer;
import com.vsdata.melsec.server.MelsecTcpServerConfig;

/**
 * @author liumin
 */
public class Melsec3EAsciiTcpServerExample {

    public static void main(String[] args) {
        MelsecTcpServerConfig config = new MelsecTcpServerConfig.Builder().build();
        MelsecTcpServer server = MelsecTcpServer.create3EAscii(config);
        server.bind("localhost", 6000);
    }
}
