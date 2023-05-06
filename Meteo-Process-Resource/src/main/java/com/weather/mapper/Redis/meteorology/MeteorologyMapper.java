package com.weather.mapper.Redis.meteorology;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeteorologyMapper  {

    List<String[]> getMeteorologyDataByTime(String key,long startTimestamp, long endTimestamp);
    List<String[]> getMeteorologyDataByDate(String station,long startTimestamp, long endTimestamp, String start_date, String end_date);
}
