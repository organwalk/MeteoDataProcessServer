package com.weather.mapper.Redis.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.mapper.Redis.RedisRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {
    private final RedisTemplate<String ,String> redisTemplate;

    @SneakyThrows
    @Override
    public Boolean saveHourMeteoCache(String dataSource, String date_hour, String which, List<List<String>> meteoData) {
        redisTemplate.opsForHash().put(dataSource,
                date_hour + "_" + which,
                new ObjectMapper().writeValueAsString(meteoData)
        );
        return true;
    }

    @SneakyThrows
    @Override
    public List<List<String>> getHourMeteoCache(String dataSource, String date_hour, String which) {
        String strMeteo = (String) redisTemplate.opsForHash().get(dataSource,date_hour + "_" + which);
        return  strMeteo != null ?
                new ObjectMapper().readValue(strMeteo, new TypeReference<>() {}) : new ArrayList<>();
    }

    @SneakyThrows
    @Override
    public Boolean saveDayMeteoCache(String dataSource, String date, String which, String type, List<List<String>> meteoData) {
        redisTemplate.opsForHash().put(dataSource,
                date + "_" + type + "_" + which,
                new ObjectMapper().writeValueAsString(meteoData)
        );
        return true;
    }

    @SneakyThrows
    @Override
    public List<List<String>> getDayMeteoCache(String dataSource, String date, String which, String type) {
        String strMeteo = (String) redisTemplate.opsForHash().get(dataSource, date + "_" + type + "_" + which);
        return strMeteo != null ?
                new ObjectMapper().readValue(strMeteo, new TypeReference<>() {}) : new ArrayList<>();
    }

    @SneakyThrows
    @Override
    public Boolean saveDateRangeCache(String dataSource, String start, String end, String which, List<List<String>> meteoData) {
        redisTemplate.opsForHash().put(dataSource,
                start + "_" + end + "_" + which,
                new ObjectMapper().writeValueAsString(meteoData)
        );
        return true;
    }

    @SneakyThrows
    @Override
    public List<List<String>> getDateRangeCache(String dataSource, String start, String end, String which) {
        String strMeteo = (String) redisTemplate.opsForHash().get(dataSource,start + "_" + end + "_" + which);
        return strMeteo != null ?
                new ObjectMapper().readValue(strMeteo, new TypeReference<>() {}) : new ArrayList<>();
    }
}
