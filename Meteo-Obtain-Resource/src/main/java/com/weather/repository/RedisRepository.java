package com.weather.repository;

import java.util.Set;

public interface RedisRepository {
    String getToken(String username);
    String voidToken(String username);
    String getAllStationCode(String key);
    Boolean ifInRange(String station,String end);
    Set getMeteoData(String station, String date);
}
