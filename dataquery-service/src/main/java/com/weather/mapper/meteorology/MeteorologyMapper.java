package com.weather.mapper;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MeteorologyMapper {
    Set<String> getMeteorologyDataByHour(String date, long startTimestamp, long endTimestamp);
}
