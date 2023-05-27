package com.weather.mapper;

import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.nio.ByteBuffer;

@Component
public class AllStationCodeMapper {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

   public String getAllStationCode(String key) {
       String stationCode = redisTemplate.opsForHash().keys(key).toString();
       String stationCode1 = stationCode.replaceAll("\\[|\\]", "").trim();
       return stationCode1.replaceAll("\\]","").trim();
   }
}
