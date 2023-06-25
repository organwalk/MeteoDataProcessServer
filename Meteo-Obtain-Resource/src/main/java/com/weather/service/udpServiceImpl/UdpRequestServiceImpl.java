package com.weather.service.udpServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.client.UDPClient;
import com.weather.entity.request.*;
import com.weather.repository.RedisRepository;
import com.weather.service.UdpRequestService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class UdpRequestServiceImpl implements UdpRequestService {
    private final UDPClient udpClient;
    private final RedisRepository repository;

    private final String appId = "root";

    private final String secret = "123456";

    @SneakyThrows
    public boolean getToken(String userName){
        String getTokenRequest = new ObjectMapper()
                .writeValueAsString(new GetToken(1,appId,secret));
        if(repository.getToken(userName) == null){
            log.info(getTokenRequest);
            udpClient.send(getTokenRequest);
        }
        return true;
    }

    @SneakyThrows
    public boolean voidToken(String userName){
        String voidTokenRequest = new ObjectMapper()
                .writeValueAsString(new VoidToken(3,repository.getToken(userName)));
        log.info(voidTokenRequest);
        udpClient.send(voidTokenRequest);
        return repository.voidToken(userName) != null ? true : false;
    }

    @SneakyThrows
    public boolean getAllStationCode(String name) {
        String getAllStationCodeRequest = new ObjectMapper()
                .writeValueAsString(new GetAllStationCode(5,repository.getToken(name)));
        log.info(getAllStationCodeRequest);
        udpClient.send(getAllStationCodeRequest);
        return true;
    }

    @SneakyThrows
    public boolean getAllStationDataRange(String name, String station) {
        String getStationDataRangeRequest = new ObjectMapper()
                .writeValueAsString(new GetStationDateRange(7,repository.getToken(name),station));
        log.info(getStationDataRangeRequest);
        udpClient.send(getStationDataRangeRequest);
        return true;
    }

    @SneakyThrows
    public boolean getMeteoData(String name, String station, String start, String end) {
        String getMeteoDataRequest = new ObjectMapper()
                .writeValueAsString(new GetMeteoData(9,repository.getToken(name),station,start,end));
        if ((repository.ifInRange(station,end))) {
            log.info(getMeteoDataRequest);
            udpClient.send(getMeteoDataRequest);
            return true;
        }else {
            log.info("Request date is out of range");
            return false;
        }
    }
}
