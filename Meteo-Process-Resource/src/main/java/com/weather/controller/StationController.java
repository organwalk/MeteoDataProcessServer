package com.weather.controller;

import com.weather.service.station.StationService;
import com.weather.utils.Result;
import com.weather.utils.StationDateResult;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qx")
@AllArgsConstructor
public class StationController {
    private final StationService stationService;

    @GetMapping("/stations")
    public Result getStationInfo(@RequestParam(name = "station", required = false) String station,
                                 @RequestHeader(name = "name") String authorization){
        return station == null
                ? stationService.getStationInfo(authorization)
                : stationService.getStationDateByStationId(station,authorization);
    }

    @GetMapping("/collection_year")
    public StationDateResult getCollectionYear(@RequestParam String station){
        return stationService.getCollectionYear(station);
    }

    @GetMapping("/collection_month")
    public StationDateResult getCollectionMonth(@RequestParam String station,
                                                @RequestParam String year){
        return stationService.getCollectionMonth(station,year);
    }

    @GetMapping("/collection_day")
    public StationDateResult getCollectionDay(@RequestParam String station,
                                                @RequestParam String year,
                                              @RequestParam String month){
        return stationService.getCollectionDay(station,year,month);
    }

    @GetMapping("/data_sum")
    public StationDateResult getDataSumByMonth(@RequestParam String station,
                                               @RequestParam String year,
                                               @RequestParam String month){
        return stationService.getStationDataSum(station,year,month);
    }
}
