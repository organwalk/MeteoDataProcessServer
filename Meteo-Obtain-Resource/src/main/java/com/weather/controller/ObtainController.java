package com.weather.controller;

import com.weather.mapper.SaveToMySQLMapper;
import com.weather.repository.RedisRepository;
import com.weather.service.UdpRequestService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;


/**
 * 此处提供了内部Http端点供服务调用
 * 服务调用使用了SpringBoot3内置的Http客户端，即WebFlux
 * by organwalk 2023.05.28
 **/

@RestController
@RequestMapping("/api/obtain")
@AllArgsConstructor
public class ObtainController {
    private final UdpRequestService udpRequestService;
    private final RedisRepository repository;
    private final SaveToMySQLMapper mapper;

    @GetMapping("/token/user")
    public boolean getToken(@RequestParam String name){
        udpRequestService.getToken(name);
        return nowGetTokenStatus(name);
    }

    @PostMapping("/token")
    public boolean voidToken(@RequestParam String name){
        return udpRequestService.voidToken(name);
    }

    @GetMapping("/meteo/station")
    public boolean getStationCode(@RequestParam String name) {
        udpRequestService.getAllStationCode(name);
        return nowGetStationCodeStatus("allStationCode:station&name");
    }

    @GetMapping("/meteo/date_range")
    public boolean getDateRange(@RequestParam String name, String station) {
        return udpRequestService.getAllStationDataRange(name,station);
    }

    @GetMapping("/meteo/data")
    public boolean getMeteoData(@RequestParam String name,
                                @RequestParam String station,
                                @RequestParam String start,
                                @RequestParam String end) {
        udpRequestService.getMeteoData(name, station, start, end);
        return nowGetMeteoData(station+"_meteo_data",start);
    }

    @SneakyThrows
    public Boolean nowGetTokenStatus(String name) {
        while (repository.getToken(name) == "") {
            Thread.sleep(1000);
        }
        return true;
    }

    @SneakyThrows
    public Boolean nowGetStationCodeStatus(String key){
        while (repository.getAllStationCode(key) == ""){
            Thread.sleep(1000);
        }
        return true;
    }

    @SneakyThrows
    public Boolean nowGetMeteoData(String table, String date){
        while (mapper.checkMeteoDataExist(table,date) == null){
            Thread.sleep(3000);
        }
        return true;
    }


}
