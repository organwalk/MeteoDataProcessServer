package com.weather;

import com.weather.client.UDPClient;
import com.weather.service.udpService.MeteoDataService;
import com.weather.service.udpService.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class MeteoObtainApplication implements CommandLineRunner {
    private final UDPClient udpClient;
    private final TokenService tokenService;
    private final MeteoDataService meteoDataService;

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
