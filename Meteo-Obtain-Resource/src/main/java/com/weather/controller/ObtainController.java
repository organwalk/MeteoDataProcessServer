package com.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.entity.message.ReqUdpMsg;
import com.weather.listener.TaskStatusListener;
import com.weather.service.UdpRequestService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 此处提供了内部Http端点供服务调用
 * * 服务调用使用了SpringBoot3内置的Http客户端，即WebFlux
 * by organwalk 2023.05.28
 **/

@RestController
@RequestMapping("/api/obtain")
@AllArgsConstructor
public class ObtainController {
    private final UdpRequestService udpRequestService;
    private final RabbitTemplate rabbitTemplate;
    private final TaskStatusListener taskStatusListener;

    @GetMapping("/token/user")
    public boolean getToken(@RequestParam String name) throws Exception {
        rabbitTemplate
                .convertAndSend("udp-req-exchange", "udp-req-routing-key",
                        new ObjectMapper().writeValueAsString(new ReqUdpMsg("getToken", name)));
        return nowTaskStatus();
    }

    @PostMapping("/token")
    public boolean voidToken(@RequestParam String name) throws Exception {
        return udpRequestService.voidToken(name);
    }

    @SneakyThrows
    @GetMapping("/meteo/station")
    public boolean getStationCode(@RequestParam String name) {
        rabbitTemplate
                .convertAndSend("udp-req-exchange", "udp-req-routing-key",
                        new ObjectMapper().writeValueAsString(new ReqUdpMsg("stationCode", name)));
        return nowTaskStatus();
    }

    @SneakyThrows
    @GetMapping("/meteo/date_range")
    public boolean getDateRange(@RequestParam String name, String station) {
        rabbitTemplate
                .convertAndSend("udp-req-exchange", "udp-req-routing-key",
                        new ObjectMapper().writeValueAsString(new ReqUdpMsg("dateRange", name, station)));
        return nowTaskStatus();
    }

    @SneakyThrows
    @GetMapping("/meteo/data")
    public boolean getMeteoData(@RequestParam String name,
                                @RequestParam String station,
                                @RequestParam String start,
                                @RequestParam String end) {
        rabbitTemplate
                .convertAndSend("udp-req-exchange", "udp-req-routing-key",
                        new ObjectMapper().writeValueAsString(new ReqUdpMsg("meteoData", name, station, start, end)));
        return nowTaskStatus();
    }

    @SneakyThrows
    public Boolean nowTaskStatus() {
        while (!taskStatusListener.isTaskOver()) {
            Thread.sleep(100);
        }
        return taskStatusListener.isTaskOver();
    }
}
