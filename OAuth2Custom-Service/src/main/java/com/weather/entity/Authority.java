package com.weather.entity;

import lombok.Data;
import lombok.NonNull;

@Data
public class Authority {

    private Integer id;
    @NonNull
    private String authority;
}

