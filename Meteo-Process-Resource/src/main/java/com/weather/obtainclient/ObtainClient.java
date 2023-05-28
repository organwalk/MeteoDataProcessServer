package com.weather.obtainclient;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("http://localhost:9494/api/obtain")
public interface ObtainClient {
    @GetExchange("/token/user")
    boolean getToken(@RequestParam String name);
    @GetExchange("/meteo/station")
    boolean getStationCode(@RequestParam String name);

    @GetExchange("/meteo/date_range")
    boolean getDateRange(@RequestParam String name,
                         @RequestParam String station);

    @GetExchange("/meteo/data")
    boolean getData(@RequestParam String name,
                    @RequestParam String station,
                    @RequestParam String start,
                    @RequestParam String end);
}
