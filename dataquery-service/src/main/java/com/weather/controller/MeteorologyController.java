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
}
