package com.weather.handler.response;

import com.weather.mapper.SaveToMySQLMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@AllArgsConstructor
public class ResponseHandlerImpl implements ResponseHandler{
    private final RedisTemplate<String, String> redisTemplate;
    private final SaveToMySQLMapper mapper;
    @Override
    public void saveToken(String username, String token) {
        String key = "tokens";
        redisTemplate.opsForHash().put(key, username, token);
        String savedToken = (String) redisTemplate.opsForHash().get(key, username);
        System.out.println("Token saved to Redis: " + savedToken);
    }

    @Override
    public void deleteToken(String token) {
        String key = "token:" + token;
        redisTemplate.delete(key);
        System.out.println("The token '" + token +  "' stored in Redis has been deleted");
    }

    @Override
    public void saveAllStationCode(String data) {
        List<String> dataList = new ArrayList<>();
        int startPos = data.indexOf("[{");
        int endPos = data.lastIndexOf("}]");
        if (startPos >= 0 && endPos >= 0) {
            String[] stationData = data.substring(startPos + 2, endPos).split("\\},\\{");
            for (String station : stationData) {
                Map<String, String> stationMap = new HashMap<>();
                String[] keyValuePairs = station.split(",");
                for (String pair : keyValuePairs) {
                    String[] parts = pair.split(":");
                    if (parts.length == 2) {
                        String key = parts[0].replaceAll("\"", "").trim();
                        String value = parts[1].replaceAll("\"", "").trim();
                        stationMap.put(key, value);
                    }
                }
                dataList.add(stationMap.get("station") + "," + stationMap.get("name"));
            }
        }

        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        for (int i = 0; i < dataList.size(); i++) {
            String stationData = dataList.get(i);
            String[] parts = stationData.split(",");
            if (parts.length == 2) {
                String stationKey = parts[0];
                String stationName = parts[1];
                ops.put("allStationCode:station&name", stationKey, stationName);

            }
        }

    }

    @Override
    public void saveMeteoDateRange(String d_station, String meteoDateRange) {
        List<String> meteoDateRangeList = new ArrayList<>();
        String[] meteoDateRangeData = meteoDateRange.split(",");
        for (String date : meteoDateRangeData){
            String date1 = date.replaceAll("\"","").trim();
            String date2 = date1.replaceAll("\\]", "").trim();
            meteoDateRangeList.add(date2.replaceAll("\\[","").trim());
        }
        for (int i = 0; i < meteoDateRangeList.size(); i ++){
            String date = meteoDateRangeList.get(i);
            redisTemplate.opsForSet().add(d_station + ":dateRange", date);
        }
    }


    @Override
    public void saveMeteoData(String station, String date, String data) {
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
//        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        for (int i = 0; i < dataList.size(); i++) {
            String element = dataList.get(i).toString();
            // 将时间字符串与日期字符串拼接
            String dateTimeStr = date.substring(1, 11) + " " + dataList.get(i).get(0).substring(1, 9);
            //样例: 2023-04-01 00:00:00
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
            ZoneId beijingZone = ZoneId.of("Asia/Shanghai"); // Use the time zone for Beijing
            ZonedDateTime beijingDateTime = ZonedDateTime.of(dateTime, beijingZone);
            long unixTimestamp = beijingDateTime.toEpochSecond(); // Get Unix timestamp in seconds
            // 用TypedTuple封装 member 和 score
//            tuples.add(new DefaultTypedTuple(element, (double) unixTimestamp));
            ops.add(key, element, unixTimestamp);
        }
//        ops.add(key, tuples);
    }
}
