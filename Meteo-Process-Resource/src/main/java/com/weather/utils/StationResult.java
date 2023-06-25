package com.weather.utils;

import lombok.Builder;
import lombok.Data;

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

    public static StationResult fail(){
        return StationResult.builder()
                .success(ResultCode.FAIL)
                .build();
    }

}
