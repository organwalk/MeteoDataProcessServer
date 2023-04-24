package com.weather.utils;

import lombok.Data;
import java.util.List;

@Data
public class MeteorologyResult {
    private int success;
    private String station;
    private Object data;

    public static MeteorologyResult success(String station, Object data){
        MeteorologyResult meteorologyResult = new MeteorologyResult();
        meteorologyResult.setSuccess(ResultCode.SUCCESS);
        meteorologyResult.setStation(station);
        meteorologyResult.setData(data);
        return meteorologyResult;
    }

    public static MeteorologyResult fail(){
        MeteorologyResult meteorologyResult = new MeteorologyResult();
        meteorologyResult.setSuccess(ResultCode.FAIL);
        return meteorologyResult;
    }
}
