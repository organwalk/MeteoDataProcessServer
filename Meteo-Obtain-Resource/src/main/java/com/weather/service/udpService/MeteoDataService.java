package com.weather.service.udpService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.client.UDPClient;
import com.weather.entity.request.GetAllStationCode;
import com.weather.entity.request.GetMeteoData;
import com.weather.entity.request.GetStationDateRange;
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
    private MeteoDateRangeMapper meteoDateRangeMapper;

    public boolean getAllStationCode(String name) throws Exception {
        int code = 5;
        String token = tokenMapper.getToken(name);
        GetAllStationCode getAllStationCode = new GetAllStationCode(code,token);
        ObjectMapper mapper = new ObjectMapper();
        String getAllStationCodeRequest = mapper.writeValueAsString(getAllStationCode);
        udpClient.send(getAllStationCodeRequest);
        System.out.println(getAllStationCodeRequest);
        return true;
    }

    public boolean getAllStationDataRange(String name,String station) throws Exception {
        int code = 7;
        String token = tokenMapper.getToken(name);
        GetStationDateRange getStationDateRange = new GetStationDateRange(code,token,station);
        ObjectMapper mapper = new ObjectMapper();
        String getStationDataRangeRequest = mapper.writeValueAsString(getStationDateRange);
        udpClient.send(getStationDataRangeRequest);
        System.out.println(getStationDataRangeRequest);
        return true;
    }

    public boolean getMeteoData(String name,String station,String start,String end) throws Exception {
        int code = 9;
        String token = tokenMapper.getToken(name);

        GetMeteoData getMeteoData = new GetMeteoData(code,token,station,start,end);
        ObjectMapper mapper = new ObjectMapper();
        String getMeteoDataRequest = mapper.writeValueAsString(getMeteoData);
        if ((meteoDateRangeMapper.ifInRange(station,end))) {
            System.out.println(getMeteoDataRequest);
            udpClient.send(getMeteoDataRequest);
            return true;
        }else {
            System.out.println("请求日期超出范围");
        }
        return false;
    }
}
