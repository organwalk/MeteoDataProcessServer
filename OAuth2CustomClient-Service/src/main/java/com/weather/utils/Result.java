package com.weather.utils;

import lombok.Data;

@Data
public class Result {
    private Integer success;
    private String code;

    public static Result success(String code){
        Result result = new Result();
        result.setSuccess(ResultCode.SUCCESS);
        result.setCode(code);
        return result;
    }

    public static Result fail(){
        Result result = new Result();
        result.setSuccess(ResultCode.Fail);
        return result;
    }
}
