package com.weather.mapper.redis;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRedis {
    void saveToken(String username,String token);
    void voidAccessToken(String username);
    Object checkAccessToken(String username);
}
