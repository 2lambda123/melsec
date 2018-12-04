package com.vsdata.melsec.server;

import java.util.concurrent.CompletableFuture;

/**
 * @author liumin
 */
public interface MelsecTcpServer {

    /**
     * 创建一个3E Ascii的服务端
     *
     * @param config 配置
     * @return 服务实例
     */
    static MelsecTcpServer create3EAscii(MelsecTcpServerConfig config) {
        return new Melsec3EAsciiTcpServer(config);
    }

    /**
     * 创建一个3E Binary的服务端
     *
     * @param config 配置
     * @return 服务实例
     */
    static MelsecTcpServer create3EBinary(MelsecTcpServerConfig config) {
        return new Melsec3EBinaryTcpServer(config);
    }

    /**
     * 绑定到主机和端口
     *
     * @param host 主机
     * @param port 端口
     * @return 结果Future
     */
    CompletableFuture<MelsecTcpServer> bind(String host, int port);

    /**
     * 关闭
     */
    void shutdown();
}
