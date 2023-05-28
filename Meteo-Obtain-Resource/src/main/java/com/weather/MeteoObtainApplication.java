package com.weather;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class MeteoObtainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeteoObtainApplication.class);
    }

}
