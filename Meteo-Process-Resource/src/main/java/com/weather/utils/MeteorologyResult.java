package com.weather.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeteorologyResult {
    private int success;
    private String station;
    private Object data;

    public static MeteorologyResult success(String station, Object data){
        return MeteorologyResult.builder()
                .success(ResultCode.SUCCESS)
                .station(station)
                .data(data)
                .build();
    }

    public static MeteorologyResult fail(){
        return MeteorologyResult.builder()
                .success(ResultCode.FAIL)
                .build();
    }
}
