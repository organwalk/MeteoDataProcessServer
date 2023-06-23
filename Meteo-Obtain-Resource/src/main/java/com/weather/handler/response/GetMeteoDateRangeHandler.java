package com.weather.handler.response;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import java.util.List;


@Component
@AllArgsConstructor
public class GetMeteoDateRangeHandler {
    private final RedisTemplate<String, String> redisTemplate;
    public void saveMeteoDateRangeToRedis(String d_station,String meteoDateRange) {
        List<String> meteoDateRangeList = new ArrayList<>();
        String[] meteoDateRangeData = meteoDateRange.split(",");
        for (String date : meteoDateRangeData){
            String date1 = date.replaceAll("\"","").trim();
            String date2 = date1.replaceAll("\\]", "").trim();
            meteoDateRangeList.add(date2.replaceAll("\\[","").trim());
        }

        for (int i = 0; i < meteoDateRangeList.size(); i ++){
            String date = meteoDateRangeList.get(i);
            redisTemplate.opsForList().rightPush(d_station + ":dateRange",date);
        }
    }
}
