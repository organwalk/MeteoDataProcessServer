package com.weather.handler.response;

public interface ResponseHandler {
    void saveToken(String username,String token);
    void deleteToken(String token);
    void saveAllStationCode(String data);
    void saveMeteoDateRange(String d_station,String meteoDateRange);
    void saveMeteoData(String station, String date, String data);
}
