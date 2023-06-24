package com.weather.repository.repositoryimpl;

import com.weather.repository.RedisRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {
    private final RedisTemplate<String ,String> redisTemplate;
    @Override
    public String getToken(String username) {
        return (String) redisTemplate.opsForHash().get("tokens",username);
    }

    @Override
    public String voidToken(String username) {
        return String.valueOf(redisTemplate.opsForHash().delete("tokens",username));
    }

    @Override
    public String getAllStationCode(String key) {
        return redisTemplate.opsForHash().keys(key).toString().replaceAll("\\[|\\]", "").trim();
    }

    @Override
    public Boolean ifInRange(String station, String end) {
        return redisTemplate.opsForSet().isMember(station+":dateRange", end);
    }
}
