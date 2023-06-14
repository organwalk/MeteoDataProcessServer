package com.weather.mapper.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserRedisImpl implements UserRedis{
    private final RedisTemplate redisTemplate;
    @Override
    public void saveToken(String username,String token) {
        redisTemplate.opsForHash().put("Meteo-UserClient-User",username,token);
    }

    @Override
    public void voidAccessToken(String username) {
        redisTemplate.opsForHash().delete("Meteo-UserClient-User",username);
    }

    @Override
    public String checkAccessToken(String username) {
        return (String) redisTemplate.opsForHash().get("Meteo-UserClient-User",username);
    }
}
