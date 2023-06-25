package com.weather.entity.respond;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRespond {
    private Integer code;
    private Boolean success;
    private Auth auth;

    @Data
    @Builder
    static class Auth {
        private String name;
        private String access_token;
    }

    public static LoginRespond ok (String name,String accessToken){
        Auth auth = Auth.builder()
                .name(name)
                .access_token(accessToken)
                .build();
        return LoginRespond.builder()
                .code(200)
                .success(true)
                .auth(auth)
                .build();
    }

    public static LoginRespond not_found(){
        return LoginRespond.builder()
                .code(404)
                .success(false)
                .build();
    }

    public static LoginRespond fail (){
        return LoginRespond.builder()
                .code(500)
                .success(false)
                .build();
    }
}
