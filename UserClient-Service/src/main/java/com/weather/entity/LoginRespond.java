package com.weather.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRespond {
    private Integer code;
    private Boolean success;
    private Object accessToken;

    public static LoginRespond ok (String accessToken){
        return LoginRespond.builder()
                .code(200)
                .success(true)
                .accessToken(accessToken)
                .build();
    }

    public static LoginRespond not_found(){
        return LoginRespond.builder()
                .code(404)
                .success(false)
                .accessToken("user can not found")
                .build();
    }

    public static LoginRespond fail (){
        return LoginRespond.builder()
                .code(500)
                .success(false)
                .accessToken("auth fail")
                .build();
    }
}
