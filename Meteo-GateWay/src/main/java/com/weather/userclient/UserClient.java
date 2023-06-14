package com.weather.userclient;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("http://localhost:9194/user")
public interface UserClient {
    @GetExchange("/check")
    String checkAccessToken(@RequestParam String username);
}
