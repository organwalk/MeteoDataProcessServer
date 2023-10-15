package com.weather.utils;

import lombok.Builder;
import lombok.Data;

/**
 * 定义获取气象站有效日期接口响应
 * by organwalk 2023-04-08
 */
@Data
@Builder
public class StationDateResult implements Result{
    private int success;
    private Object data;

    // 成功响应
    public static StationDateResult success(Object data){
        return StationDateResult.builder()
                .success(ResultCode.SUCCESS)
                .data(data)
                .build();
    }

    // 失败响应
    public static StationDateResult fail(Object data){
        return StationDateResult.builder()
                .success(ResultCode.FAIL)
                .data(data)
                .build();
    }
}
