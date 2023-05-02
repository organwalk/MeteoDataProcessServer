package com.weather.entity.request;

import lombok.Data;

@Data
public class GetMeteoData {
    int code;
    String token;
    String start;
    String end;

    public GetMeteoData(int code, String token, String start, String end) {
        this.code = code;
        this.token = token;
        this.start = start;
        this.end = end;
    }
}
