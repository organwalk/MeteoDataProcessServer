package com.weather.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MeteoDateRangeMapper {
    @Autowired
    private RedisTemplate<String ,String> redisTemplate;

    public Boolean ifInRange(String station,String end){
        List<String> dateRangeList = new ArrayList<>();
        String[] dateData = redisTemplate.opsForList().range(station+":dateRange", 0, -1).toString().split(",");
        for (String date : dateData){
            dateRangeList.add(date.replaceAll("\\[","").replaceAll("\\]","").trim());
        }
        return dateRangeList.contains(end);
    }
}
