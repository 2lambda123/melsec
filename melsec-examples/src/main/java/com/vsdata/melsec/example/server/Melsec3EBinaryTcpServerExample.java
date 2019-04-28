package com.vsdata.melsec.example.server;

import com.vsdata.melsec.server.MelsecTcpServer;
import com.vsdata.melsec.server.MelsecTcpServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liumin
 */
public class Melsec3EBinaryTcpServerExample {

    private static final Logger LOG = LoggerFactory.getLogger(Melsec3EBinaryTcpServerExample.class);

    public static void main(String[] args) {
        MelsecTcpServerConfig config = new MelsecTcpServerConfig.Builder().build();
        MelsecTcpServer server = MelsecTcpServer.create3EBinary(config);
        LOG.info("starting server in 3000...");
        server.bind("localhost", 3000);
    }
}
