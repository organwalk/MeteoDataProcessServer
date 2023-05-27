package com.weather.handler.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GetAllStationCodeHandler {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public GetAllStationCodeHandler(RedisTemplate<String , String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void saveAllStationCodeToRedis(String data) {
        List<String> dataList = new ArrayList<>();
        int startPos = data.indexOf("[{");
        int endPos = data.lastIndexOf("}]");
        if (startPos >= 0 && endPos >= 0) {
            String[] stationData = data.substring(startPos + 2, endPos).split("\\},\\{");
            for (String station : stationData) {
                Map<String, String> stationMap = new HashMap<>();
                String[] keyValuePairs = station.split(",");
                for (String pair : keyValuePairs) {
                    String[] parts = pair.split(":");
                    if (parts.length == 2) {
                        String key = parts[0].replaceAll("\"", "").trim();
                        String value = parts[1].replaceAll("\"", "").trim();
                        stationMap.put(key, value);
                    }
                }
                dataList.add(stationMap.get("station") + "," + stationMap.get("name"));
            }
        }

        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        for (int i = 0; i < dataList.size(); i++) {
            String stationData = dataList.get(i);
            String[] parts = stationData.split(",");
            if (parts.length == 2) {
                String stationKey = parts[0];
                String stationName = parts[1];
                ops.put("allStationCode:station&name", stationKey, stationName);
            }
        }

    }
}
