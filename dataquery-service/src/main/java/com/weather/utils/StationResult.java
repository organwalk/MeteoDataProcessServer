package com.weather.utils;

import lombok.Data;

@Data
public class StationResult implements Result{
    private int success;
    private Object station;

    public static StationResult success(Object station){
        StationResult stationResult = new StationResult();
        stationResult.setSuccess(ResultCode.SUCCESS);
        stationResult.setStation(station);
        return stationResult;
    }

    public static StationResult fail(){
        StationResult stationResult = new StationResult();
        stationResult.setSuccess(ResultCode.FAIL);
        return stationResult;
    }

}
