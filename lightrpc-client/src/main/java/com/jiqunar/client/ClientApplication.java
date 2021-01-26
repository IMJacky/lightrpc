package com.jiqunar.client;

import com.jiqunar.common.HelloService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 客户端启动类
 *
 * @author jieguang.wang
 * @date 2021/1/15 16:25
 */
@SpringBootApplication
public class ClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
        HelloService helloService = ProxyFactory.create(HelloService.class);
        System.out.println(helloService.hello("王杰光"));
    }
}
