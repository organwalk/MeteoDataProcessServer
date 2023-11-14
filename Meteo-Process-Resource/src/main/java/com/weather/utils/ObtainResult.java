package com.weather.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObtainResult implements Result{
    private int success;
    private Object data;

    // 成功响应
    public static ObtainResult success(Object data){
        return ObtainResult.builder()
                .success(ResultCode.SUCCESS)
                .data(data)
                .build();
    }

    // 失败响应
    public static ObtainResult fail(Object data){
        return ObtainResult.builder()
                .success(ResultCode.FAIL)
                .data(data)
                .build();
    }
}
