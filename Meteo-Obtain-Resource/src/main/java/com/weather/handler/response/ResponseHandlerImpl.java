package com.weather.handler.response;

import com.weather.callback.DateRangeCallback;
import com.weather.callback.MeteoDataCallback;
import com.weather.callback.SaveTokenCallback;
import com.weather.callback.StationCodeCallback;
import com.weather.entity.table.MeteoData;
import com.weather.mapper.SaveToMySQLMapper;
import com.weather.repository.RedisRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ResponseHandlerImpl implements ResponseHandler {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SaveToMySQLMapper mapper;
    @Autowired
    private  RedisRepository repository;
    private boolean tokenSaved = false;
    private boolean stationCodeSaved = false;
    private boolean dateRangeSaved = false;
    private static final Logger logger = LogManager.getLogger(ResponseHandlerImpl.class);

    private SaveTokenCallback saveTokenCallback;
    public void setSaveTokenCallback(SaveTokenCallback saveTokenCallback) {
        this.saveTokenCallback = saveTokenCallback;
    }

    private StationCodeCallback stationCodeCallback;

    public void setStationCodeCallback(StationCodeCallback callback) {
        this.stationCodeCallback = callback;
    }

    private DateRangeCallback dateRangeCallback;
    public void setDateRangeCallback(DateRangeCallback dateRangeCallback) {
        this.dateRangeCallback = dateRangeCallback;
    }


    @SneakyThrows
    @Override
    public void saveToken(String username, String token) {
        redisTemplate.opsForHash().put("tokens", username, token);
        String savedToken = (String) redisTemplate.opsForHash().get("tokens", username);
        tokenSaved = true; // 令牌保存成功
        logger.info("Token saved to Redis: " + savedToken);
        // 调用回调方法通知操作完成
        if (saveTokenCallback != null) {
            saveTokenCallback.onTokenSaved(tokenSaved);
        }

    }

    @Override
    public boolean isTokenSaved() {
        return tokenSaved;
    }

    @Override
    public void deleteToken(String token) {
        String key = "token:" + token;
        redisTemplate.delete(key);
        logger.info("The token '" + token + "' stored in Redis has been deleted");
    }

    /**
     * 保存气象站点至数据库中
     * @param data 数据存储服务器的响应字符串
     */
    @SneakyThrows
    @Override
    public void saveAllStationCode(String data) {
        logger.info("开始将气象站点数据保存至数据库中");
        extractData(data).forEach(dataItem -> {
            mapper.insertStation(dataItem.station, dataItem.name);
        });
        stationCodeSaved = true; // 气象站编号保存成功
        logger.info("The stationCode has been successfully saved to database");
        // 调用回调方法通知操作完成
        if (stationCodeCallback != null) {
            stationCodeCallback.onStationCodeSaved(stationCodeSaved);
        }
    }

    @Override
    public boolean isStationCodeSave() {
        return stationCodeSaved;
    }

    /**
     * 将气象站点相关响应字符串处理并返回一个包含气象站编号和名称的列表
     * @param input 字符串
     * @return 包含气象站点和编号的对象列表
     */
    private static List<DataItem> extractData(String input) {
        List<DataItem> extractedData = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"station\":\\s*\"(.*?)\",\\s*\"name\":\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String station = matcher.group(1);
            String name = matcher.group(2);
            extractedData.add(new DataItem(station, name));
        }
        return extractedData;
    }

    @Data
    @AllArgsConstructor
    private static class DataItem {
        private String station;
        private String name;
    }

    @SneakyThrows
    @Override
    public void saveMeteoDateRange(String d_station, String meteoDateRange) {
        List<String> meteoDateRangeList = new ArrayList<>();
        String[] meteoDateRangeData = meteoDateRange.split(",");
        for (String date : meteoDateRangeData) {
            String saveDate = date.replaceAll("\"", "").trim().replaceAll("\\]", "").trim();
            meteoDateRangeList.add(saveDate.replaceAll("\\[", "").trim());
        }
        for (int i = 0; i < meteoDateRangeList.size(); i++) {
            String date = meteoDateRangeList.get(i);
            redisTemplate.opsForSet().add(d_station + ":dateRange", date);
            mapper.insertStationDateRange(date, d_station);
        }
        logger.info("The MeteoDateRange has been successfully saved to Redis");
        // 调用回调方法通知操作完成
        dateRangeSaved = true;
        if (dateRangeCallback != null) {
            dateRangeCallback.onDateRangeSaved(dateRangeSaved);
        }
    }

    @Override
    public boolean isDateRangeSave() {
        return dateRangeSaved;
    }


    @SneakyThrows
    @Override
    public void saveMeteoData(int last, String station, String date, String data) {
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
        logger.info("[进行]--正在将响应气象数据保存至缓存中");
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();
        String key = station + "_data_" + date.replace("\"", "");
        for (List<String> strings : dataList) {
            ops.add(key, strings.toString(),
                    ZonedDateTime.of(
                                    LocalDateTime
                                            .parse(date.substring(1, 11) + " " + strings.get(0).substring(1, 9),
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                    , ZoneId.of("Asia/Shanghai"))
                            .toEpochSecond());
        }
        logger.info("[完成]--已成功将响应气象数据保存至缓存中");
        if (last == 1){
            logger.info("[进行]--正在将响应气象数据保存至数据库中");
            saveMeteoToMySQL(station, date);
            logger.info("[完成]--已成功将响应气象数据保存至数据库中");
        }
    }


    @SneakyThrows
    public void saveMeteoToMySQL(String station, String date) {
        Set<ZSetOperations.TypedTuple<String>> meteo = repository.getMeteoData(station, date.replace("\"", ""));
        List<MeteoData> meteoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (ZSetOperations.TypedTuple<String> tuple : meteo) {
            List<String> value = Arrays.asList(tuple.getValue().replaceAll("\\[|\\]", "").split(","));
            meteoList.add(new MeteoData(
                    station,
                    sdf.parse(date.replace("\"", "")),
                    new Date((long) (tuple.getScore() * 1000)),
                    Time.valueOf(value.get(0).replace("\"", "")),
                    Float.parseFloat(value.get(1).replace("\"", "")),
                    Float.parseFloat(value.get(2).replace("\"", "")),
                    Float.parseFloat(value.get(3).replace("\"", "")),
                    Float.parseFloat(value.get(4).replace("\"", "")),
                    Float.parseFloat(value.get(5).replace("\"", "")),
                    Float.parseFloat(value.get(6).replace("\"", "")),
                    Float.parseFloat(value.get(7).replace("\"", "")),
                    Float.parseFloat(value.get(8).replace("\"", ""))
            ));
        }
        mapper.insertMeteoData(String.format(station + "_meteo_data"), meteoList);
    }
}
