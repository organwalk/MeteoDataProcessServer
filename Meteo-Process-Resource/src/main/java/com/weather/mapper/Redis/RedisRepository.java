package com.weather.mapper.Redis;

import java.util.List;

public interface RedisRepository {
    Boolean saveHourMeteoCache(String dataSource, String date_hour, String which,List<List<String>> meteoData);
    List<List<String>> getHourMeteoCache(String dataSource, String date_hour, String which);
    Boolean saveDayMeteoCache(String dataSource, String date, String which, String type, List<List<String>> meteoData);
    List<List<String>> getDayMeteoCache(String dataSource, String date, String which, String type);
    Boolean saveDateRangeCache(String dataSource, String start, String end, String which,List<List<String>> meteoData);
    List<List<String>> getDateRangeCache(String dataSource, String start, String end, String which);
}
