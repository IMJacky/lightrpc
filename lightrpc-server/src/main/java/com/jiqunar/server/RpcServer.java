package com.jiqunar.server;

import com.jiqunar.common.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author jieguang.wang
 * @date 2021/1/15 15:56
 */
@Component
public class RpcServer implements InitializingBean {
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    @Autowired
    private ServerHandler serverHandler;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 负责客户端连接的线程池
        bossGroup = new NioEventLoopGroup();
        // 负责读写的线程池
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // 添加编码器
                        pipeline.addLast(new RpcEncoder(RpcRequest.class, new FastJsonSerializer()));
                        // 添加解码器
                        pipeline.addLast(new RpcDecoder(RpcResponse.class, new FastJsonSerializer()));
                        // 请求处理类
                        pipeline.addLast(serverHandler);
                    }
                });
        ChannelFuture future = serverBootstrap.bind(6666).sync();
        System.out.println("NettyServer启动成功");
        future.channel().closeFuture().sync();
    }

    @PreDestroy
    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
