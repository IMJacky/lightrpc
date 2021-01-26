package com.jiqunar.client;

import com.jiqunar.common.RpcRequest;
import com.jiqunar.common.RpcResponse;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端数据处理
 *
 * @author jieguang.wang
 * @date 2021/1/15 13:55
 */
public class ClientHandler extends ChannelDuplexHandler {
    // 请求ID与响应结果fultured的集合
    private final Map<String, DefaultFuture> futureMap = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcResponse) {
            RpcResponse rpcResponse = (RpcResponse) msg;
            DefaultFuture defaultFuture = futureMap.get(rpcResponse.getRequestId());
            // 获取之后把结果写入defaultFuture
            defaultFuture.setRpcResponse(rpcResponse);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof RpcRequest) {
            RpcRequest rpcRequest = (RpcRequest) msg;
            // 发送之前，初始化一个future
            futureMap.putIfAbsent(rpcRequest.getRequestId(), new DefaultFuture());
        }
        super.write(ctx, msg, promise);
    }

    public RpcResponse getRpcResponse(String requestId) {
        try {
            DefaultFuture defaultFuture = futureMap.get(requestId);
            return defaultFuture.getRpcResponse(500000);
        } finally {
            futureMap.remove(requestId);
        }
    }
}
