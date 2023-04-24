package com.weather;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.weather.mapper.MySQL")
public class DataQueryApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataQueryApplication.class);
    }
}
