package com.weather.utils;

import lombok.Data;

@Data
public class LogoutResult {
    private int success;

    public static LogoutResult success(){
        LogoutResult logoutResult = new LogoutResult();
        logoutResult.setSuccess(ResultCode.SUCCESS);
        return logoutResult;
    }
    public static LogoutResult fail(){
        LogoutResult logoutResult = new LogoutResult();
        logoutResult.setSuccess(ResultCode.FAIL);
        return logoutResult;
    }
}
