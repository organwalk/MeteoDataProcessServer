package com.weather.repository;

public interface RedisRepository {
    String getToken(String username);
    String voidToken(String username);
    String getAllStationCode(String key);
    Boolean ifInRange(String station,String end);
}
