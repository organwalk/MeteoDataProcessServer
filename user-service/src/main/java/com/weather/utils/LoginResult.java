package com.weather.utils;

import lombok.Data;

@Data
public class LoginResult {
    private int success;
    private int id;


    public static LoginResult success(int id){
        LoginResult loginResult = new LoginResult();
        loginResult.setSuccess(ResultCode.SUCCESS);
        loginResult.setId(id);
        return loginResult;
    }

    public static LoginResult fail(){
        LoginResult loginResult = new LoginResult();
        loginResult.setSuccess(ResultCode.FAIL);
        return loginResult;
    }
}
