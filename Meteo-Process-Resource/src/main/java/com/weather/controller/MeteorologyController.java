package com.weather.controller;

import com.weather.service.meteorology.MeteorologyService;
import com.weather.utils.MeteorologyResult;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 气象数据查询接口
 * by organwalk 2023-04-09
 */
@RestController
@RequestMapping("/qx")
@AllArgsConstructor
@Validated
public class MeteorologyController {
    private final MeteorologyService meteorologyService;

    // 获取任一小时的分钟气象信息
    @PostMapping("/stat_hour")
    public MeteorologyResult getMeteorologyByHour(@RequestParam @NotBlank(message = "station不能为空") String station,
                                                  @RequestParam @Pattern(
                                                          regexp = "\\d{4}-\\d{2}-\\d{2}",
                                                          message = "date字段必须是yyyy-mm-dd格式数据"
                                                  ) String date,
                                                  @RequestParam @Pattern(
                                                          regexp = "([01]?[0-9]|2[0-3])",
                                                          message = "hour字段必须是00~23的24小时格式数据"
                                                  ) String hour,
                                                  @RequestParam @Pattern(
                                                          regexp = "^(?:[1-8],?)+$",
                                                          message = "which字段必须是1-8范围内的可选要素"
                                                  ) String which,
                                                  @RequestParam @Min(value = 1, message = "pageSize必须为大于1的整数")
                                                      @Digits(integer = Integer.MAX_VALUE, fraction = 0)
                                                      int pageSize,
                                                  @RequestParam @Min(value = 0, message = "offset必须为大于或等于0的整数")
                                                      @Digits(integer = Integer.MAX_VALUE, fraction = 0) int offset){
        return meteorologyService.getMeteorologyByHour(station,date,hour,which,pageSize,offset);
    }

    // 获取任一天的小时气象数据
    @PostMapping("/stat_day")
    public MeteorologyResult getMeteorologyByDay(@RequestParam @NotBlank(message = "station不能为空") String station,
                                                  @RequestParam @Pattern(
                                                          regexp = "\\d{4}-\\d{2}-\\d{2}",
                                                          message = "date字段必须是yyyy-mm-dd格式数据"
                                                  ) String date,
                                                  @RequestParam @Pattern(
                                                          regexp = "^(?:[1-8],?)+$",
                                                          message = "which字段必须是1-8范围内的可选要素"
                                                  ) String which,
                                                 @RequestParam @Pattern(
                                                         regexp = "^([12])$",
                                                         message = "type字段必须是1或2"
                                                  ) String type){
        return meteorologyService.getMeteorologyByDay(station,date,which,type);
    }

    // 获取任意时间段以天为单位的气象数据
    @PostMapping("/stat_day_range")
    public MeteorologyResult getMeteorologyByDayRange(@RequestParam String station,
                                                 @RequestParam String start_date,
                                                 @RequestParam String end_date,
                                                 @RequestParam String which,
                                                 @RequestParam int pageSize,
                                                 @RequestParam int offset,
                                                 @RequestHeader(name = "name") String authorization){
        return meteorologyService.getMeteorologyByDate(authorization,station,start_date,end_date,which,pageSize,offset);
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
                                                   @RequestParam(required = false) String end_pm10,
                                                   @RequestParam int pageSize,
                                                   @RequestParam int offset,
                                                   @RequestHeader(name = "name") String authorization){
        return meteorologyService.getComplexMeteorology(authorization,
                                                        station,
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
                                                        end_pm10,pageSize,offset);
    }
}
