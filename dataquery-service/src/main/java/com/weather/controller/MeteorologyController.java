package com.weather.controller;

import com.weather.service.meteorology.MeteorologyService;
import com.weather.utils.MeteorologyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qx")
public class MeteorologyController {
    @Autowired
    MeteorologyService meteorologyService;
    @PostMapping("/stat_hour")
    public MeteorologyResult getMeteorologyByHour(@RequestParam String station,
                                                  @RequestParam String date,
                                                  @RequestParam String hour,
                                                  @RequestParam String which){
        return meteorologyService.getMeteorologyByHour(station,date,hour,which);
    }
    @PostMapping("/stat_day")
    public MeteorologyResult getMeteorologyByDay(@RequestParam String station,
                                                  @RequestParam String date,
                                                  @RequestParam String which){
        return meteorologyService.getMeteorologyByDay(station,date,which);
    }
    @PostMapping("/stat_day_range")
    public MeteorologyResult getMeteorologyByDay(@RequestParam String station,
                                                 @RequestParam String start_date,
                                                 @RequestParam String end_date,
                                                 @RequestParam String which){
        return meteorologyService.getMeteorologyByDate(station,start_date,end_date,which);
    }
    @PostMapping("/correlation")
    public MeteorologyResult corrcoefDate(@RequestParam String station,
                                                 @RequestParam String start_date,
                                                 @RequestParam String end_date,
                                                 @RequestParam String correlation){
        return meteorologyService.corrcoefDate(station,start_date,end_date,correlation);
    }
    @PostMapping("/query")
    public MeteorologyResult getComplexMeteorology(@RequestParam String station,
                                                   @RequestParam String start_date,
                                                   @RequestParam String end_date,
                                                   @RequestParam(required = false) String start_temperature,
                                                   @RequestParam(required = false) String end_temperature,
                                                   @RequestParam(required = false) String start_humidity,
                                                   @RequestParam(required = false) String end_humidity,
                                                   @RequestParam(required = false) String start_speed,
                                                   @RequestParam(required = false) String end_speed,
                                                   @RequestParam(required = false) String start_direction,
                                                   @RequestParam(required = false) String end_direction,
                                                   @RequestParam(required = false) String start_rain,
                                                   @RequestParam(required = false) String end_rain,
                                                   @RequestParam(required = false) String start_sunlight,
                                                   @RequestParam(required = false) String end_sunlight,
                                                   @RequestParam(required = false) String start_pm25,
                                                   @RequestParam(required = false) String end_pm25,
                                                   @RequestParam(required = false) String start_pm10,
                                                   @RequestParam(required = false) String end_pm10){
        return meteorologyService.getComplexMeteorology(station,
                                                        start_date,
                                                        end_date,
                                                        start_temperature,
                                                        end_temperature,
                                                        start_humidity,
                                                        end_humidity,
                                                        start_speed,
                                                        end_speed,
                                                        start_direction,
                                                        end_direction,
                                                        start_rain,
                                                        end_rain,
                                                        start_sunlight,
                                                        end_sunlight,
                                                        start_pm25,
                                                        end_pm25,
                                                        start_pm10,
                                                        end_pm10);
    }
}
