package com.weather.mapper.Redis.meteorology.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.mapper.Redis.meteorology.MeteorologyMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Repository
@AllArgsConstructor
public class MeteorologyMapperImpl implements MeteorologyMapper {

    private final RedisTemplate<String, String> redisTemplate;

    // 创建 ObjectMapper 实例，用于序列化和反序列化 JSON
    ObjectMapper objectMapper = new ObjectMapper();
    // 创建一个列表，用于保存获取到的气象数据

    /**
     * 根据小时获取气象数据
     * @param key Redis 中的键名
     * @param startTimestamp 开始时间戳（单位：秒）
     * @param endTimestamp 结束时间戳（单位：秒）
     * @return 返回气象数据的列表，每个元素是一个字符串数组
     */
    @Override
    public List<String[]> getMeteorologyDataByTime(String key, long startTimestamp, long endTimestamp) {
        return redisTemplate.execute(new RedisCallback<List<String[]>>() {
            @Override
            public List<String[]> doInRedis(RedisConnection connection) throws DataAccessException {
                // 获取 Redis 中指定范围内的有序集合
                Set<byte[]> set = connection.zRangeByScore(key.getBytes(), startTimestamp, endTimestamp);
                List<String[]> dataList = new ArrayList<>();
                for (byte[] data : set) {
                    try {
                        // 将 JSON 字节数组转换为字符串数组，并添加到 dataList 列表中
                        String[] dataArray = objectMapper.readValue(data, String[].class);
                        dataList.add(dataArray);
                    } catch (IOException e) {
                        // 如果转换出错，抛出 RuntimeException 异常
                        throw new RuntimeException(e);
                    }
                }
                if (!dataList.isEmpty()) {
                    return dataList;
                } else {
                    return null;
                }
            }
        });
    }

    @Override
    public List<String[]> getMeteorologyDataByDate(String station, long startTimestamp, long endTimestamp, String start_date, String end_date) {
        return redisTemplate.execute(new RedisCallback<List<String[]>>() {
            @Override
            public List<String[]> doInRedis(RedisConnection connection) throws DataAccessException {
                List<String[]> dataList = new ArrayList<>();
                for (String date : datesBetween(start_date, end_date)) {
                    String key = station + "_data_" + date;
                    // 获取 Redis 中指定范围内的有序集合
                    Set<byte[]> set = connection.zRangeByScore(key.getBytes(), startTimestamp, endTimestamp);
                    if (!set.isEmpty()){
                        for (byte[] data : set) {
                            try {
                                // 将 JSON 字节数组转换为字符串数组，并添加到 dataList 列表中
                                String[] dataArray = objectMapper.readValue(data, String[].class);
                                dataList.add(dataArray);
                            } catch (IOException e) {
                                // 如果转换出错，抛出 RuntimeException 异常
                                throw new RuntimeException(e);
                            }
                        }
                    }else {
                        dataList.clear();
                    }
                }
                return dataList;
            }
        });
    }

    public List<String> datesBetween(String startDateStr, String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        List<String> dates = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            dates.add(startDate.toString());
            startDate = startDate.plus(1, ChronoUnit.DAYS);
        }
        return dates;
    }
}
