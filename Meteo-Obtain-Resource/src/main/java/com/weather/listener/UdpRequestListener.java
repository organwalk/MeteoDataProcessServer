package com.weather.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.entity.message.ReqUdpMsg;
import com.weather.service.UdpRequestService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UdpRequestListener {
    private final UdpRequestService service;

    @SneakyThrows
    @RabbitListener(queues = "udp-req-queue")
    public void handleMessage(String jsonMsg) {
        ReqUdpMsg msg = new ObjectMapper().readValue(jsonMsg, ReqUdpMsg.class);
        switch (msg.getMsgType()) {
            case "getToken" -> service.getToken(msg.getName());
            case "stationCode" -> service.getAllStationCode(msg.getName());
            case "dateRange" -> service.getAllStationDataRange(msg.getName(),msg.getStation());
            case "meteoData" -> service.getMeteoData(msg.getName(),msg.getStation(),msg.getStart(),msg.getEnd());
        }
    }
}
