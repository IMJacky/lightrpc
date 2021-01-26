package com.jiqunar.client;

import com.jiqunar.common.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.annotation.PreDestroy;

/**
 * rpc客户端
 *
 * @author jieguang.wang
 * @date 2021/1/15 11:55
 */
public class RpcClient {
    private NioEventLoopGroup eventLoopGroup;
    private ClientHandler clientHandler;
    private Channel channel;
    private String host;
    private Integer port;
    private static final int MAX_RETRY = 5;

    public RpcClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        clientHandler = new ClientHandler();
        eventLoopGroup = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // 添加编码器
                        pipeline.addLast(new RpcEncoder(RpcRequest.class, new FastJsonSerializer()));
                        // 添加解码器
                        pipeline.addLast(new RpcDecoder(RpcResponse.class, new FastJsonSerializer()));
                        // 请求处理类
                        pipeline.addLast(clientHandler);
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(host, port);
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else {
                System.err.println("连接失败!");
            }
        });
        channel = channelFuture.channel();
    }

    public RpcResponse send(final RpcRequest rpcRequest) {
        try {
            channel.writeAndFlush(rpcRequest).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return clientHandler.getRpcResponse(rpcRequest.getRequestId());
    }

    @PreDestroy
    public void close() {
        eventLoopGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
    }
}
