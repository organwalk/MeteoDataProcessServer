package com.weather.utils;

import lombok.Data;

@Data
public class StationDateResult implements Result{
    private int success;
    private Object date;

    public static StationDateResult success(Object date){
        StationDateResult stationDateResult = new StationDateResult();
        stationDateResult.setSuccess(ResultCode.SUCCESS);
        stationDateResult.setDate(date);
        return stationDateResult;
    }

    public static StationDateResult fail(){
        StationDateResult stationDateResult = new StationDateResult();
        stationDateResult.setSuccess(ResultCode.FAIL);
        return stationDateResult;
    }
}
