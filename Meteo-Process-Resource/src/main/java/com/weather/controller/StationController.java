package com.weather.controller;

import com.weather.obtainclient.ObtainClient;
import com.weather.service.station.StationDateService;
import com.weather.service.station.StationService;
import com.weather.utils.MeteorologyResult;
import com.weather.utils.Result;
import com.weather.utils.StationDateResult;
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
    private final ObtainClient obtainClient;

    @GetMapping("/stations")
    public Result getStationInfo(@RequestParam(name = "station", required = false) String station,
                                 @RequestHeader(name = "Authorization") String authorization){
        //调用数据获取服务
        String tokenValue = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            tokenValue = authorization.substring(7);
            obtainClient.getToken(tokenValue);
        }
        if (station == null){
            obtainClient.getStationCode(tokenValue);
            return stationService.getStationInfo();
        }else {
            obtainClient.getDateRange(tokenValue,station);
            //获取时间范围逻辑
            obtainClient.getData(tokenValue,station,"2023-04-04","2023-04-05");
            return stationDateService.getStationDateByStationId(station);
        }

    }

    @GetMapping("/collection_year")
    public StationDateResult getCollectionYear(@RequestParam String station){
        return stationDateService.getCollectionYear(station);
    }

    @GetMapping("/collection_month")
    public StationDateResult getCollectionMonth(@RequestParam String station,
                                                @RequestParam String year){
        return stationDateService.getCollectionMonth(station,year);
    }

    @GetMapping("/collection_day")
    public StationDateResult getCollectionDay(@RequestParam String station,
                                                @RequestParam String year,
                                              @RequestParam String month){
        return stationDateService.getCollectionDay(station,year,month);
    }

    @GetMapping("/data_sum")
    public StationDateResult getDataSumByMonth(@RequestParam String station,
                                               @RequestParam String year,
                                               @RequestParam String month){
        return stationDateService.getStationDataSum(station,year,month);
    }
}
