package com.weather.handler.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Component
public class GetTokenHandler {
    private static final String TOKEN_KEY_PREFIX = "token:";
    private static final long EXPIRATION_TIME = 7;
    private static final TimeUnit TIME_UNIT = TimeUnit.DAYS;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    public void saveTokenToRedis(String token) {
        String key = TOKEN_KEY_PREFIX + token;
        byte[] tokenBytes = token.getBytes(StandardCharsets.UTF_8);
        String tokenStr = new String(tokenBytes, StandardCharsets.UTF_8);

        redisTemplate.execute((RedisCallback<String>) connection -> {
            connection.set(key.getBytes(), tokenStr.getBytes());
            connection.expire(key.getBytes(), TIME_UNIT.toSeconds(EXPIRATION_TIME));
            return null;
        });

        String savedToken = redisTemplate.opsForValue().get(key);
        System.out.println("Token saved to Redis: " + savedToken);
    }
}
