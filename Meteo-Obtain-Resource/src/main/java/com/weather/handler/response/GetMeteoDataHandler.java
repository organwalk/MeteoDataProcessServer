package com.weather.handler.response;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class GetMeteoDataHandler {
    private final RedisTemplate<String, String> redisTemplate;

    public void saveMeteoDataToRedis(String station, String date, String data) {
        String key = station + "_data_" + date;
        List<List<String>> dataList = new ArrayList<>();
        int startPos = data.indexOf("[[");
        int endPos = data.lastIndexOf("]]");
        if (startPos >= 0 && endPos >= 0) {
            String[] dataArray = data.substring(startPos + 2, endPos).split("\\],\\[");
            for (String item : dataArray) {
                item = item.replaceAll("\\[|\\]", "");
                String[] values = item.split(",");
                dataList.add(Arrays.asList(values));
            }
        }
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();
        for (int i = 0; i < dataList.size(); i++) {
            String element = dataList.get(i).toString();
            // 将时间字符串与日期字符串拼接
            String dateTimeStr = date.substring(1, 11) + " " + dataList.get(i).get(0).substring(1, 9);
            //样例: 023-04-01 00:00:00
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
            ZoneId beijingZone = ZoneId.of("Asia/Shanghai"); // Use the time zone for Beijing
            ZonedDateTime beijingDateTime = ZonedDateTime.of(dateTime, beijingZone);
            long unixTimestamp = beijingDateTime.toEpochSecond(); // Get Unix timestamp in seconds
            ops.add(key, element, unixTimestamp);
        }
    }
}
