package com.weather.utils;

import lombok.Data;

@Data
public class Result {
    private Integer success;
    private Object data;

    public static Result success(Object data){
        Result result = new Result();
        result.setSuccess(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result fail(){
        Result result = new Result();
        result.setSuccess(ResultCode.Fail);
        return result;
    }
}
