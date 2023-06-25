package com.weather.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StationDateResult implements Result{
    private int success;
    private Object date;

    public static StationDateResult success(Object date){
        return StationDateResult.builder()
                .success(ResultCode.SUCCESS)
                .date(date)
                .build();
    }

    public static StationDateResult fail(){
        return StationDateResult.builder()
                .success(ResultCode.FAIL)
                .build();
    }
}
