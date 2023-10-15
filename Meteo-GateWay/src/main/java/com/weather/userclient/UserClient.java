package com.weather.userclient;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * 获取用户服务内部接口
 * by organwalk 2023-04-02
 */
@HttpExchange("http://localhost:9194/user")
public interface UserClient {
    @GetExchange("/token")
    String getAccessToken(@RequestParam String username);
}
