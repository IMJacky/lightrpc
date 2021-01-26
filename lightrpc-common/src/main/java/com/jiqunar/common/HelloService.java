package com.jiqunar.common;

/**
 * 服务接口定义
 *
 * @author jieguang.wang
 * @date 2021/1/15 16:29
 */
@RpcService
public interface HelloService {
    String hello(String name);
}
