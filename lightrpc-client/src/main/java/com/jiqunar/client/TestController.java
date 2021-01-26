package com.jiqunar.client;

import com.jiqunar.common.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jieguang.wang
 * @date 2021/1/26 15:17
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("hello")
    public String test() {
        HelloService helloService = new ClientDynamicProxy<>(HelloService.class).getProxy();
        return helloService.hello("王杰光");
    }
}
