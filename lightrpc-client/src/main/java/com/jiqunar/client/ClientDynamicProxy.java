package com.jiqunar.client;

import com.jiqunar.common.RpcRequest;
import com.jiqunar.common.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author jieguang.wang
 * @date 2021/1/15 16:41
 */
public class ClientDynamicProxy<T> implements InvocationHandler {
    private Class<T> clazz;

    public ClientDynamicProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameters(args);

        RpcClient rpcClient = new RpcClient("127.0.0.1", 6666);
        rpcClient.connect();
        RpcResponse rpcResponse = rpcClient.send(rpcRequest);
        return rpcResponse.getResult();
    }
}
