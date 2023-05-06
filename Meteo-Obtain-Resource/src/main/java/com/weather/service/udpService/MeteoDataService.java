package com.weather.service.udpService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.client.UDPClient;
import com.weather.entity.request.GetAllStationCode;
import com.weather.entity.request.GetMeteoData;
import com.weather.entity.request.GetStationDateRange;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MeteoDataService {
    private final UDPClient udpClient;

    public void getAllStationCode() throws Exception {
        int code = 5;
        String token = "asdfghjklzxcvbnm";

        GetAllStationCode getAllStationCode = new GetAllStationCode(code,token);
        ObjectMapper mapper = new ObjectMapper();
        String getAllStationCodeRequest = mapper.writeValueAsString(getAllStationCode);
        udpClient.send(getAllStationCodeRequest);
    }

    public void getAllStationDataRange() throws Exception {
        int code = 7;
        String token = "asdfghjklzxcvbnm";
        String station = "m2_403";

        GetStationDateRange getStationDateRange = new GetStationDateRange(code,token,station);
        ObjectMapper mapper = new ObjectMapper();
        String getStationDataRangeRequest = mapper.writeValueAsString(getStationDateRange);
        udpClient.send(getStationDataRangeRequest);
    }

    public void getMeteoData() throws Exception {
        int code = 9;
        String token = "asdfghjklzxcvbnm";
        String start = "2023-04-01";
        String end = "2023-04-03";

        GetMeteoData getMeteoData = new GetMeteoData(code,token,start,end);
        ObjectMapper mapper = new ObjectMapper();
        String getMeteoDataRequest = mapper.writeValueAsString(getMeteoData);
        udpClient.send(getMeteoDataRequest);
    }
}
