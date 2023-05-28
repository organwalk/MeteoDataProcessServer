package com.weather.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TokenMapper {
    @Autowired
    private RedisTemplate<String ,String> redisTemplate;

    public String getToken(String username) {
            return (String) redisTemplate.opsForHash().get("tokens",username);
    }
}
