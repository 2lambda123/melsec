package com.vsdata.melsec.client;

import com.vsdata.melsec.message.e.FrameECommand;
import com.vsdata.melsec.message.e.FrameEResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.CompletableFuture;

/**
 * @author liumin
 */
public interface MelsecTcpClient {

    /**
     * 创建一个3E Ascii的client
     *
     * @param config Melsec Client配置项
     * @return Melsec Client实例
     */
    static MelsecTcpClient create3EAscii(MelsecClientConfig config) {
        return new Melsec3EAsciiTcpClient(config);
    }

    /**
     * 创建一个3E Binary的client
     *
     * @param config Melsec Client配置项
     * @return Melsec Client实例
     */
    static MelsecTcpClient create3EBinary(MelsecClientConfig config) {
        return new Melsec3EBinaryTcpClient(config);
    }

    /**
     * 启动
     *
     * @return 结果Future
     */
    CompletableFuture<Channel> bootstrap();

    /**
     * 连接
     *
     * @return 结果Future
     */
    CompletableFuture<MelsecTcpClient> connect();

    /**
     * 端口连接
     *
     * @return 结果Future
     */
    CompletableFuture<MelsecTcpClient> disconnect();

    /**
     * 发送请求
     *
     * @param request 请求命令
     * @param <T>     返回类型
     * @return 结果Future
     */
    <T extends FrameEResponse> CompletableFuture<T> sendRequest(FrameECommand request);

    /**
     * 读数据处理
     *
     * @param ctx      处理器上下文
     * @param response 返回消息
     */
    void onChannelRead(ChannelHandlerContext ctx, FrameEResponse response);

    /**
     * 异常处理
     *
     * @param ctx   处理器上下文
     * @param cause 异常
     */
    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause);

    /**
     * 批量读取
     *
     * @param address 软元件地址
     * @param points  数据点数
     * @return 结果Future
     */
    CompletableFuture<ByteBuf> batchRead(String address, int points);

    /**
     * 批量写入
     *
     * @param address 软元件地址
     * @param points  数据点数
     * @param data    写入数据
     * @return 结果Future
     */
    CompletableFuture<Void> batchWrite(String address, int points, ByteBuf data);

}
