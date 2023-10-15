package com.weather.utils;

import lombok.Data;

/**
 * 通用响应实体
 * by organwalk 2023-04-02
 */
@Data
public class Result {
    private Integer success;
    private Object data;

    /**
     * 表示成功状态下的响应结果
     * @param data 数据
     * @return 表示成功响应的Result对象
     *
     * by organwalk 2023-04-02
     */
    public static Result success(Object data){
        Result result = new Result();
        result.setSuccess(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    /**
     * 表示失败状态下的响应结果
     * @return 表示失败响应的Result对象
     *
     * by organwalk 2023-04-02
     */
    public static Result fail(Object data){
        Result result = new Result();
        result.setSuccess(ResultCode.Fail);
        result.setData(data);
        return result;
    }
}
