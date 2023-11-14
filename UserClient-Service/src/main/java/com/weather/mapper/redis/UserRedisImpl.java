package com.weather.mapper.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 定义用户信息的Redis接口实现
 * by organwalk 2023-04-02
 */
@Component
@AllArgsConstructor
public class UserRedisImpl implements UserRedis{
    private final RedisTemplate redisTemplate;
    private final static String HASH_KEY = "Meteo-UserClient-User";

    /**
     * 执行保存Token的操作
     * @param username 用户名
     * @param token 通行令牌
     *
     * by organwalk 2023-04-02
     */
    @Override
    public void saveToken(String username,String token) {
        redisTemplate.opsForHash().put(HASH_KEY,username,token);
    }

    /**
     * 执行废弃Token的操作
     * @param username 用户名
     *
     * by organwalk 2023-04-02
     */
    @Override
    public void voidAccessToken(String username) {
        redisTemplate.opsForHash().delete(HASH_KEY,username);
    }

    /**
     * 获取用户登录状态
     * @param username 用户名
     * @return 用户登录状态
     *
     * by organwalk 2023-04-02
     */
    @Override
    public Boolean checkUserStatus(String username) {
        return redisTemplate.opsForHash().hasKey(HASH_KEY, username);
    }

    /**
     * 获取令牌内容
     * @param username 用户名
     * @return Token内容
     *
     * by organwalk 2023-04-02
     */
    @Override
    public String getAccessToken(String username) {
        return (String) redisTemplate.opsForHash().get(HASH_KEY,username);
    }
}
