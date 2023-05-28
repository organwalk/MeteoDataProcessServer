package com.weather.handler.response;

import com.weather.mapper.TokenMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class GetTokenHandler {

    private final RedisTemplate<String, String> redisTemplate;
    public void saveTokenToRedis(String username,String token) {
        String key = "tokens";
        redisTemplate.opsForHash().put(key, username, token);
        String savedToken = (String) redisTemplate.opsForHash().get(key, username);
        System.out.println("Token saved to Redis: " + savedToken);
    }
}
