package com.weather.handler.response;

import com.alibaba.fastjson.JSONObject;
import com.weather.callback.DateRangeCallback;
import com.weather.callback.MeteoDataCallback;
import com.weather.callback.SaveTokenCallback;
import com.weather.callback.StationCodeCallback;

public interface ResponseHandler {
    void setSaveTokenCallback(SaveTokenCallback saveTokenCallback);
    void setStationCodeCallback(StationCodeCallback callback);
    void setDateRangeCallback(DateRangeCallback dateRangeCallback);
    void setMeteoDataCallback(MeteoDataCallback meteoDataCallback);
    void saveToken(String username,String token);
    boolean isTokenSaved();
    void deleteToken(String token);
    void saveAllStationCode(String data);
    boolean isStationCodeSave();
    void saveMeteoDateRange(String d_station,String meteoDateRange);
    boolean isDateRangeSave();
    void saveMeteoData(int last, String station, String date, String data);
    boolean isMeteoDataSave();
}
