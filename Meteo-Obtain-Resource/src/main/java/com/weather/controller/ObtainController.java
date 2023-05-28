package com.weather.controller;

import com.weather.service.udpService.MeteoDataService;
import com.weather.service.udpService.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** 此处提供了内部Http端点供服务调用
 ** 服务调用使用了SpringBoot3内置的Http客户端，即WebFlux
 *  by organwalk 2023.05.28
 **/

@RestController
@RequestMapping("/api/obtain")
@AllArgsConstructor
public class ObtainController {
    private final TokenService tokenService;
    private final MeteoDataService meteoDataService;

    //获取令牌
    @GetMapping("/token/user")
    public boolean getToken(@RequestParam String name) throws Exception {
        return tokenService.getToken(name) ? true : false;
    }

    //作废令牌
    @PostMapping("/token")
    public boolean voidToken(@RequestParam String name) throws Exception {
        return tokenService.voidToken(name) ? true : false;
    }

    //获取所有气象站编号 server -> redis -> rdb file and mysql
    @GetMapping("/meteo/station")
    public boolean getStationCode(@RequestParam String name) throws Exception {
        return meteoDataService.getAllStationCode(name);
    }

    //获取所有气象站指定日期 server -> redis -> rdb file and mysql
    @GetMapping("/meteo/date_range")
    public boolean getDateRange(@RequestParam String name,String station) throws Exception {
        return meteoDataService.getAllStationDataRange(name,station);
    }

    //获取气象站数据 server -> redis -> rdb file and mysql
    @GetMapping("/meteo/data")
    public boolean getMeteoData(@RequestParam String name,
                                @RequestParam String station,
                                @RequestParam String start,
                                @RequestParam String end) throws Exception {
        return meteoDataService.getMeteoData(name,station,start,end);
    }

}
