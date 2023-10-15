package com.weather.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置项
 * by organwalk 2023-04-02
 */
@Configuration
@AllArgsConstructor
public class RedisConfig {
    private final RedisTemplate redisTemplate;

    /**
     * 主要进行了序列化配置，避免键、值、哈希键和哈希值的乱码问题
     * @return redisTemplate对象
     *
     * by organwalk 2023-04-02
     */
    @Bean
    public RedisTemplate redisTemplateInit() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
