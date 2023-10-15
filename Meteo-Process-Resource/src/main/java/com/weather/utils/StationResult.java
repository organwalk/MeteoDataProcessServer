package com.weather.utils;

import lombok.Builder;
import lombok.Data;

/**
 * 定义获取气象站列表接口的响应
 * by organwalk 2023-04-08
 */
@Data
@Builder
public class StationResult implements Result{
    private int success;
    private Object station;

    public static StationResult success(Object station){
        return StationResult.builder()
                .success(ResultCode.SUCCESS)
                .station(station)
                .build();
    }

    public static StationResult fail(Object station){
        return StationResult.builder()
                .success(ResultCode.FAIL)
                .station(station)
                .build();
    }

}
