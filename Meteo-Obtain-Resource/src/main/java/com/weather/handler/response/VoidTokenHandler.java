package com.weather.handler.response;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class VoidTokenHandler {
    private static final String TOKEN_KEY_PREFIX = "token:";
    private final RedisTemplate<String ,String> redisTemplate;

    public void deleteTokenInRedis(String token){
        String key = TOKEN_KEY_PREFIX + token;

        redisTemplate.delete(key);
        System.out.println("存在redis的token：" + token + " 已被删除");
    }
}
