package com.jiqunar.server;

import com.jiqunar.common.RpcRequest;
import com.jiqunar.common.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author jieguang.wang
 * @date 2021/1/15 16:03
 */
@Component
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());

        try {
            Class<?> clazz = Class.forName(rpcRequest.getClassName());
            Object serviceBean = applicationContext.getBean(clazz);
            Class<?> serviceClass = serviceBean.getClass();

            FastClass fastClass = FastClass.create(serviceClass);
            FastMethod fastMethod = fastClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            Object result = fastMethod.invoke(serviceBean, rpcRequest.getParameters());
            rpcResponse.setResult(result);
        } catch (Throwable throwable) {
            rpcResponse.setError(throwable.toString());
            throwable.printStackTrace();
        }
        channelHandlerContext.writeAndFlush(rpcResponse);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
