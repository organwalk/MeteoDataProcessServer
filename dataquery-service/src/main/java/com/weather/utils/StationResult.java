package com.weather.utils;

import lombok.Data;

@Data
public class Result {
    private int success;
    private Object station;

    public static Result success(Object station){
        Result result = new Result();
        result.setSuccess(ResultCode.SUCCESS);
        result.setStation(station);
        return result;
    }

    public static Result fail(){
        Result result = new Result();
        result.setSuccess(ResultCode.FAIL);
        return result;
    }

}
