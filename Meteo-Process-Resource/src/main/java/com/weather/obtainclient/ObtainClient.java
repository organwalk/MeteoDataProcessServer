package com.weather.obtainclient;


import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("http://localhost:9494")
public interface ObtainClient {
    @GetExchange("/api/inside/test")
    String getTest();
}
