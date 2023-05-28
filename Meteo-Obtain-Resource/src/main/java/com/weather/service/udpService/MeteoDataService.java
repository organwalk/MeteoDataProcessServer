package com.weather.service.udpService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.client.UDPClient;
import com.weather.entity.request.GetAllStationCode;
import com.weather.entity.request.GetMeteoData;
import com.weather.entity.request.GetStationDateRange;
import com.weather.mapper.AllStationCodeMapper;
import com.weather.mapper.MeteoDateRangeMapper;
import com.weather.mapper.TokenMapper;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MeteoDataService {
    private final UDPClient udpClient;

    @Resource
    private TokenMapper tokenMapper;
    @Resource
    private AllStationCodeMapper allStationCodeMapper;
    @Resource
    private MeteoDateRangeMapper meteoDateRangeMapper;

    public void getAllStationCode() throws Exception {
        int code = 5;
        String token = tokenMapper.getToken("token:asdfghjklzxcvbnm");

        GetAllStationCode getAllStationCode = new GetAllStationCode(code,token);
        ObjectMapper mapper = new ObjectMapper();
        String getAllStationCodeRequest = mapper.writeValueAsString(getAllStationCode);
        udpClient.send(getAllStationCodeRequest);
    }

    public void getAllStationDataRange() throws Exception {
        int code = 7;
        String token = tokenMapper.getToken("token:asdfghjklzxcvbnm");
        String station = allStationCodeMapper.getAllStationCode("allStationCode:station&name");

        GetStationDateRange getStationDateRange = new GetStationDateRange(code,token,station);
        ObjectMapper mapper = new ObjectMapper();
        String getStationDataRangeRequest = mapper.writeValueAsString(getStationDateRange);
        udpClient.send(getStationDataRangeRequest);
    }

    public void getMeteoData() throws Exception {
        int code = 9;
        String token = tokenMapper.getToken("token:asdfghjklzxcvbnm");
        String start = "2023-04-01";
        String end = "2023-04-04";

        GetMeteoData getMeteoData = new GetMeteoData(code,token,start,end);
        ObjectMapper mapper = new ObjectMapper();
        String getMeteoDataRequest = mapper.writeValueAsString(getMeteoData);
        if ((meteoDateRangeMapper.ifInRange(end))) {
            udpClient.send(getMeteoDataRequest);
        }else {
            System.out.println("请求日期超出范围");
        }
    }
}
