package com.weather.obtainclient;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("http://localhost:9494/api/obtain")
public interface ObtainClient {
    @GetExchange("/token/user")
    boolean getToken(@RequestParam String name);
    @PostExchange("/token")
    boolean voidToken(@RequestParam String name);
}
