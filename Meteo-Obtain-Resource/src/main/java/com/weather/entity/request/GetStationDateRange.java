package com.weather.entity.request;

import lombok.Data;

@Data
public class GetStationDateRange {
    int code;
    String token;
    String station;

    public GetStationDateRange(int code, String token, String station) {
        this.code = code;
        this.token = token;
        this.station = station;
    }
}
