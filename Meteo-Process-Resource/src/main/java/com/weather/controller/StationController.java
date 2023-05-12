package com.weather.controller;

import com.weather.service.station.StationDateService;
import com.weather.service.station.StationService;
import com.weather.utils.Result;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/qx")
@AllArgsConstructor
public class StationController {
    private final StationService stationService;
    private final StationDateService stationDateService;

    @GetMapping("/stations")
    public Result getStationInfo(@RequestParam(name = "station", required = false) String station){
        if (station == null){
            System.out.println(station);
            return stationService.getStationInfo();
        }else {
            System.out.println(station);
            return stationDateService.getStationDateByStationId(station);
        }

    }
}
