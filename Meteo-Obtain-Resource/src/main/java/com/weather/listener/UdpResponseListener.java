package com.weather.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.entity.message.ResUdpMsg;
import com.weather.handler.response.ResponseHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UdpResponseListener {
    private final ResponseHandler resHandler;

    @SneakyThrows
    @RabbitListener(queues = "udp-res-queue")
    public void handleMessage(String jsonMsg) {
        ResUdpMsg msg = new ObjectMapper().readValue(jsonMsg, ResUdpMsg.class);
        switch (msg.getCode()) {
            case 2 -> resHandler.saveToken(msg.getUsername(),msg.getToken());
            case 6 -> resHandler.saveAllStationCode(msg.getData());
            case 8 -> resHandler.saveMeteoDateRange(msg.getStation(),msg.getDate());
            case 10 -> resHandler.saveMeteoData(msg.getLast(), msg.getStation(),msg.getDate(),msg.getData());
        }
    }
}
