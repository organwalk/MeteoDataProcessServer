package com.weather.service.udpServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.client.UDPClient;
import com.weather.controller.ObtainController;
import com.weather.entity.request.*;
import com.weather.handler.response.ResponseHandler;
import com.weather.repository.RedisRepository;
import com.weather.service.UdpRequestService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UdpRequestServiceImpl implements UdpRequestService {
    private final UDPClient udpClient;
    private final RedisRepository repository;
    private static final Logger logger = LogManager.getLogger(UdpRequestServiceImpl.class);
    private static final String appId = "root";
    private static final String secret = "123456";

    @SneakyThrows
    public boolean getToken(String userName){
        String getTokenRequest = new ObjectMapper()
                .writeValueAsString(new GetToken(1,appId,secret));
        if(repository.getToken(userName) == null){
            logger.info(getTokenRequest);
            udpClient.send(getTokenRequest);
            return false;
        }else {
            logger.info("已与远程数据存储服务器建立连接并获得授权，无需重复连接");
            return true;
        }
    }

    @SneakyThrows
    public boolean voidToken(String userName){
        String voidTokenRequest = new ObjectMapper()
                .writeValueAsString(new VoidToken(3,repository.getToken(userName)));
        logger.info(voidTokenRequest);
        udpClient.send(voidTokenRequest);
        return repository.voidToken(userName) != null ? true : false;
    }

    @SneakyThrows
    public void getAllStationCode(String name) {
        String getAllStationCodeRequest = new ObjectMapper()
                .writeValueAsString(new GetAllStationCode(5,repository.getToken(name)));
        logger.info(getAllStationCodeRequest);
        udpClient.send(getAllStationCodeRequest);
    }

    @SneakyThrows
    public boolean getAllStationDataRange(String name, String station) {
        String getStationDataRangeRequest = new ObjectMapper()
                .writeValueAsString(new GetStationDateRange(7,repository.getToken(name),Integer.valueOf(station)));
        logger.info(getStationDataRangeRequest);
        udpClient.send(getStationDataRangeRequest);
        return true;
    }

    @SneakyThrows
    public boolean getMeteoData(String name, String station, String start, String end) {
        String getMeteoDataRequest = new ObjectMapper()
                .writeValueAsString(new GetMeteoData(9,repository.getToken(name),station,start,end));
        logger.info(getMeteoDataRequest);
        udpClient.send(getMeteoDataRequest);
        return true;
    }
}
