package com.weather.controller;

import com.weather.service.station.StationDateService;
import com.weather.service.station.StationService;
import com.weather.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qx")
public class StationController {
    @Autowired
    StationService stationService;
    @Autowired
    StationDateService stationDateService;

    @PostMapping("/stations")
    public Result getStationInfo(@RequestParam(name = "id") String id,
                                 @RequestParam(name = "station", required = false) String station){
        if (station == null){
            return stationService.getStationInfo(Integer.parseInt(id));
        }else {
            return stationDateService.getStationDateByStationId(Integer.parseInt(id),station);
        }

    }
}
