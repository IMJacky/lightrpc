package com.jiqunar.server;

import com.jiqunar.common.HelloService;
import com.jiqunar.common.RpcService;
import org.springframework.stereotype.Service;

/**
 * @author jieguang.wang
 * @date 2021/1/15 16:33
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello：" + name;
    }
}
