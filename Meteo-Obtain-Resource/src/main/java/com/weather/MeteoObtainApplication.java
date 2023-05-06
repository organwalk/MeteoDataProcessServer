package com.weather;

import com.weather.client.UDPClient;
import com.weather.service.udpService.MeteoDataService;
import com.weather.service.udpService.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeteoObtainApplication implements CommandLineRunner {
    @Autowired
    private UDPClient udpClient;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private MeteoDataService meteoDataService;

    public static void main(String[] args) {
        SpringApplication.run(MeteoObtainApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {
        tokenService.getToken();
        tokenService.voidToken();
        meteoDataService.getAllStationCode();
        meteoDataService.getAllStationDataRange();
        meteoDataService.getMeteoData();
    }
}
