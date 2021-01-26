package com.jiqunar.client;

import java.lang.reflect.Proxy;

/**
 * @author jieguang.wang
 * @date 2021/1/15 16:46
 */
public class ProxyFactory {
    public static <T> T create(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new ClientDynamicProxy<T>(interfaceClass));
    }
}
