package com.weather.handler.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GetMeteoDataHandler {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void saveMeteoDataToRedis(String station, String date, String data){
        String key = station + "_data_" + date;
        List<List<String >> dataList = new ArrayList<>();
        int startPos = data.indexOf("[[");
        int endPos = data.lastIndexOf("]]");
        if(startPos >= 0 && endPos >= 0){
            String[] dataArray = data.substring(startPos + 2,endPos).split("\\],\\[");
            for (String item : dataArray){
                item = item.replaceAll("\\[|\\]","");
                String[] values = item.split(",");
                dataList.add(Arrays.asList(values));
            }
        }

        ZSetOperations<String,String> ops = redisTemplate.opsForZSet();
        for (int i = 0; i < dataList.size(); i ++){
            ops.add(key,dataList.get(i).toString(),i);
        }
    }
}
