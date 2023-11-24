package com.weather.controller;

import com.weather.client.ObtainClient;
import com.weather.entity.MeteoSyncReq;
import com.weather.mapper.MySQL.station.StationMapper;
import com.weather.service.meteorology.MeteorologyService;
import com.weather.utils.MeteorologyResult;
import com.weather.utils.ObtainResult;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

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
    private final StationMapper stationMapper;
    private final ObtainClient obtainClient;
    private static final Logger logger = LogManager.getLogger(MeteorologyController.class);

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
    public MeteorologyResult getMeteorologyByDayRange(@RequestParam @NotBlank(message = "station不能为空") String station,
                                                      @RequestParam @Pattern(
                                                              regexp = "\\d{4}-\\d{2}-\\d{2}",
                                                              message = "start_date字段必须是yyyy-mm-dd格式数据"
                                                      ) String start_date,
                                                      @RequestParam @Pattern(
                                                              regexp = "\\d{4}-\\d{2}-\\d{2}",
                                                              message = "end_date字段必须是yyyy-mm-dd格式数据"
                                                      ) String end_date,
                                                      @RequestParam @Pattern(
                                                              regexp = "^(?:[1-8],?)+$",
                                                              message = "which字段必须是1-8范围内的可选要素"
                                                      ) String which,
                                                      @RequestParam @Min(value = 1, message = "pageSize必须为大于1的整数")
                                                          @Digits(integer = Integer.MAX_VALUE, fraction = 0)
                                                          int pageSize,
                                                      @RequestParam @Min(value = 0, message = "offset必须为大于或等于0的整数")
                                                          @Digits(integer = Integer.MAX_VALUE, fraction = 0) int offset){
        return meteorologyService.getMeteorologyByDate(station,start_date,end_date,which,pageSize,offset);
    }

    // 获取指定复杂查询条件的气象数据
    @PostMapping("/query")
    public MeteorologyResult getComplexMeteorology(@RequestParam @NotBlank(message = "station不能为空") String station,
                                                   @RequestParam @Pattern(
                                                           regexp = "\\d{4}-\\d{2}-\\d{2}",
                                                           message = "start_date字段必须是yyyy-mm-dd格式数据"
                                                   ) String start_date,
                                                   @RequestParam @Pattern(
                                                           regexp = "\\d{4}-\\d{2}-\\d{2}",
                                                           message = "end_date字段必须是yyyy-mm-dd格式数据"
                                                   ) String end_date,
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
                                                   @RequestParam @Min(value = 1, message = "pageSize必须为大于1的整数")
                                                       @Digits(integer = Integer.MAX_VALUE, fraction = 0)
                                                       int pageSize,
                                                   @RequestParam @Min(value = 0, message = "offset必须为大于或等于0的整数")
                                                       @Digits(integer = Integer.MAX_VALUE, fraction = 0) int offset){
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
                                                        end_pm10,pageSize,offset);
    }

    // 与数据存储服务建立连接
    @GetMapping("/obtain/connect")
    public ObtainResult connectDataSaveServer(@RequestHeader(name = "name")String name){
        logger.info("用户" + name + "尝试与数据存储服务器建立连接");
        return obtainClient.getToken(name)
                ? ObtainResult.success("已成功与数据存储服务器建立连接")
                : ObtainResult.fail("无法数据同步，原因：未能与数据存储服务器建立连接");
    }

    // 同步气象站编号数据
    @GetMapping("/obtain/sync/station")
    public ObtainResult syncStationData(@RequestHeader(name = "name") String name){
        return obtainClient.getStationCode(name)
                ? ObtainResult.success("已成功同步更新气象站点数据")
                : ObtainResult.fail("未能同步更新气象站点数据");
    }

    // 同步气象站点有效日期
    @GetMapping("/obtain/sync/date_range")
    public ObtainResult syncDateRange(@RequestHeader(name = "name") String name){
        logger.info("[完成]--尝试同步气象站有效采集日期");
        List<String> stationList = stationMapper.getStationList();
        if (stationList.isEmpty()){
            String info = "未能同步更新气象数据有效日期";
            logger.info("[失败]--" + info);
            return ObtainResult.fail(info);
        }
        List<String> resList = new ArrayList<>();
        stationList.forEach(item -> {
            boolean res = obtainClient.getDateRange(name, item);
            resList.add(String.valueOf(res));
        });
        if (!resList.contains("true")){
            return ObtainResult.fail("气象站点暂无有效日期");
        }
        return ObtainResult.success("已成功同步更新气象数据有效日期");
    }

    // 获取气象数据最新记录日期
    @GetMapping("/obtain/sync/latest_date")
    public ObtainResult syncLatestDate(@RequestParam("station") String station){
        String dataSource = station + "_meteo_data";
        String latestDate = stationMapper.meteoDataLatestDate(dataSource, station);
        if (Objects.isNull(latestDate)){
            return ObtainResult.fail("该气象站点最新实际采集日期为空");
        }
        return ObtainResult.success(latestDate);
    }

    // 检查气象站是否未在数据库拥有数据
    @GetMapping("/obtain/sync/exist")
    public ObtainResult syncHavingData(@RequestParam("station") String station){
        String dataSource = station + "_meteo_data";
        Integer count = stationMapper.havingDataByStationCode(dataSource, station);
        if (count == 0){
            return ObtainResult.success("false");
        }
        return ObtainResult.success("true");
    }

    // 同步气象数据
    @SneakyThrows
    @PostMapping("/obtain/sync/meteo_data")
    public ObtainResult syncMeteoData(@RequestHeader(name = "name") String name,
                                      @RequestBody MeteoSyncReq req){
        logger.info("用户请求同步气象站数据");
        CompletableFuture<Boolean> future = obtainClient.getMeteoData(name, req.getStation(), req.getStart(), req.getEnd());
        boolean res = future.get();
        return res ? ObtainResult.success("已成功同步" + req.getStart() + "气象数据")
                : ObtainResult.fail("未能成功同步" + req.getStart() + "气象数据");
    }

    // 与存储服务器断开连接
    @GetMapping("/obtain/close")
    public ObtainResult closeDataSaveServer(@RequestHeader(name = "name") String name){
        return obtainClient.voidToken(name)
                ? ObtainResult.success("已成功断开与存储服务器的连接")
                : ObtainResult.fail("数据存储服务器出错");
    }
}
