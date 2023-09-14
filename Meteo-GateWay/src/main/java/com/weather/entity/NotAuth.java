package com.weather.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotAuth {
    private int code;
    private String msg;

    public static NotAuth notAuth() {
        return NotAuth.builder()
                .code(401)
                .msg("该请求认证信息无效")
                .build();
    }
}
