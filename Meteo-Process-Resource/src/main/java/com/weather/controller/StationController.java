package com.weather.controller;

import com.weather.service.station.StationService;
import com.weather.utils.MeteorologyResult;
import com.weather.utils.Result;
import com.weather.utils.StationDateResult;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 定义与气象站有关的接口
 * by organwalk 2023-04-08
 */
@RestController
@RequestMapping("/qx")
@AllArgsConstructor
@Validated
public class StationController {
    private final StationService stationService;

    // 获取气象站编号信息 or station存在时，则获取指定气象站的有效数据日期
    @GetMapping("/stations")
    public Result getStationInfo(@RequestParam(name = "station", required = false) String station){
        return station == null ?
                stationService.getStationInfo() :
                stationService.getStationDateByStationId(station);
    }

    // 获取气象站采集日期的有效年份
    @GetMapping("/collection_year")
    public StationDateResult getCollectionYear(@RequestParam @NotBlank(message = "station不能为空") String station){
        return stationService.getCollectionYear(station);
    }

    // 获取气象站采集日期的有效月份
    @GetMapping("/collection_month")
    public StationDateResult getCollectionMonth(@RequestParam @NotBlank(message = "station不能为空") String station,
                                                @RequestParam @NotBlank(message = "year不能为空") String year){
        return stationService.getCollectionMonth(station,year);
    }

    // 获取气象站采集日期的有效天数
    @GetMapping("/collection_day")
    public StationDateResult getCollectionDay(@RequestParam @NotBlank(message = "station不能为空") String station,
                                              @RequestParam @NotBlank(message = "year不能为空") String year,
                                              @RequestParam @NotBlank(message = "month不能为空") String month){
        return stationService.getCollectionDay(station,year,month);
    }

    // 获取气象站指定日期的数据总量
    @GetMapping("/data_sum")
    public MeteorologyResult getDataSumByMonth(@RequestParam @NotBlank(message = "station不能为空")String station,
                                               @RequestParam @NotBlank(message = "year不能为空") String year,
                                               @RequestParam @NotBlank(message = "month不能为空") String month){
        return stationService.getStationDataSum(station,year,month);
    }
}
