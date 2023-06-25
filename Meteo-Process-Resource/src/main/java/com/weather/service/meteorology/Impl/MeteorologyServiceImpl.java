package com.weather.service.meteorology.Impl;

import com.weather.entity.Meteorology;
import com.weather.mapper.MySQL.meteorology.*;
import com.weather.obtainclient.ObtainClient;
import com.weather.service.meteorology.MeteorologyService;
import com.weather.utils.MeteorologyResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MeteorologyServiceImpl implements MeteorologyService {
    private final HourMeteorologyMapper hourMapper;
    private final DayMeteorologyMapper dayMapper;
    private final DayToChartsMeteorologyMapper dayChartMapper;
    private final DateMeteorologyMapper dateMapper;
    private final OtherYearDateMeteorologyMapper otherYearDateMapper;
    private final ComplexMeteorologyMapper complexMapper;
    private final ObtainClient obtainClient;

    @Override
    public MeteorologyResult getMeteorologyByHour(String station, String date, String hour, String which) {
        List<List<String>> SQLResults = new ArrayList<>();
        String dataSource = station + "_weather_" + date.split("-")[0];
        String startDateTime = date + " " + hour + ":00:00";
        String endDateTime = date + " " + hour + ":59:00";
        List<Meteorology> meteorologyList = hourMapper
                .selectMeteorologyHour(dataSource, startDateTime, endDateTime, which);
        SQLResults = SQLResult(meteorologyList, "dateTime");
        return !SQLResults.isEmpty() ?
                MeteorologyResult.success(station, SQLResults) : MeteorologyResult.fail();
    }

    @Override
    public MeteorologyResult getMeteorologyByDay(String station, String date, String which, String type) {
        List<List<String>> SQLResults = new ArrayList<>();
        String dataSource = station + "_weather_" + date.split("-")[0];
        String startDateTime = date + " " + "00:00:00";
        String endDateTime = date + " " + "23:00:00";
        if (type.equals("1")) {
            List<Meteorology> meteorologyList = dayMapper
                    .selectMeteorologyDay(dataSource, startDateTime, endDateTime, which);
            SQLResults = SQLResult(meteorologyList, "dateTime");
        } else if (type.equals("2")) {
            List<Meteorology> meteorologyList = dayChartMapper
                    .selectMeteorologyDayToCharts(dataSource, startDateTime, endDateTime, which);
            SQLResults = SQLResult(meteorologyList, "dateTime");
        }
        return !SQLResults.isEmpty() ?
                MeteorologyResult.success(station, SQLResults) : MeteorologyResult.fail();
    }

    @Override
    public MeteorologyResult getMeteorologyByDate(String station, String start_date, String end_date, String which) {
        List<List<String>> SQLResults = new ArrayList<>();
        //如果查询日期处于同一年
        if (start_date.split("-")[0].equals(end_date.split("-")[0])) {
            String dataSource = station + "_weather_" + start_date.split("-")[0];
            String startDateTime = start_date + " " + "08:00:00";
            String endDateTime = end_date + " " + "08:00:00";
            List<Meteorology> meteorologyList = dateMapper
                    .selectMeteorologyDate(dataSource, startDateTime, endDateTime, which);
            SQLResults = SQLResult(meteorologyList, "date");
        } else {
            //如果查询日期不处于同一年
            String dataSourceStartDate = station + "_weather_" + start_date.split("-")[0];
            String dataSourceEndDate = station + "_weather_" + end_date.split("-")[0];
            String startDateTime = start_date + " " + "08:00:00";
            String endDateTime = end_date + " " + "08:00:00";
            List<Meteorology> meteorologyList = otherYearDateMapper
                    .selectMeteorologyDateInOtherYear(dataSourceStartDate, dataSourceEndDate, startDateTime, endDateTime, which);
            SQLResults = SQLResult(meteorologyList, "date");
        }
        return !SQLResults.isEmpty() ?
                MeteorologyResult.success(station, SQLResults) : MeteorologyResult.fail();
    }

    //4.5 获取指定复杂查询条件的气象数据
    @Override
    public MeteorologyResult getComplexMeteorology(String station,
                                                   String start_date,
                                                   String end_date,
                                                   String start_temperature,
                                                   String end_temperature,
                                                   String start_humidity,
                                                   String end_humidity,
                                                   String start_speed,
                                                   String end_speed,
                                                   String start_direction,
                                                   String end_direction,
                                                   String start_rain,
                                                   String end_rain,
                                                   String start_sunlight,
                                                   String end_sunlight,
                                                   String start_pm25,
                                                   String end_pm25,
                                                   String start_pm10,
                                                   String end_pm10) {
        List<List<String>> SQLResults = new ArrayList<>();
        List<List<String>> whichResults = new ArrayList<>();
        //如果查询日期处于同一年
        if (start_date.split("-")[0].equals(end_date.split("-")[0])) {
            String dataSource = station + "_weather_" + start_date.split("-")[0];
            List<Meteorology> meteorologyList = complexMapper
                    .selectMeteorologyComplex(dataSource, station, start_date, end_date,
                            start_temperature, end_temperature, start_humidity, end_humidity,
                            start_speed, end_speed, start_direction, end_direction,
                            start_rain, end_rain, start_sunlight, end_sunlight,
                            start_pm25, end_pm25, start_pm10, end_pm10);
            SQLResults = SQLResult(meteorologyList, "dateTime");
        }
        if (!SQLResults.isEmpty()) {
            return MeteorologyResult.success(station, SQLResults);
        } else {
            return MeteorologyResult.fail();
        }
    }


    //统一MySQL查询结果
    private List<List<String>> SQLResult(List<Meteorology> meteorologyList, String type) {
        List<List<String>> mysqlResults = new ArrayList<>();
        for (int i = 0; i < meteorologyList.size(); i++) {
            List<String> meteorologyArray = new ArrayList<>();
            Meteorology meteorology = meteorologyList.get(i);
            if (type == "dateTime") {
                if (meteorology.getDatetime() != null) {
                    meteorologyArray.add(meteorology.getDatetime());
                }
            } else if (type == "date") {
                if (meteorology.getDate() != null) {
                    meteorologyArray.add(meteorology.getDate());
                }
            }
            if (meteorology.getTemperature() != null) {
                meteorologyArray.add(meteorology.getTemperature());
            }
            if (meteorology.getHumidity() != null) {
                meteorologyArray.add(meteorology.getHumidity());
            }
            if (meteorology.getSpeed() != null) {
                meteorologyArray.add(meteorology.getSpeed());
            }
            if (meteorology.getDirection() != null) {
                meteorologyArray.add(meteorology.getDirection());
            }
            if (meteorology.getRain() != null) {
                meteorologyArray.add(meteorology.getRain());
            }
            if (meteorology.getSunlight() != null) {
                meteorologyArray.add(meteorology.getSunlight());
            }
            if (meteorology.getPm25() != null) {
                meteorologyArray.add(meteorology.getPm25());
            }
            if (meteorology.getPm10() != null) {
                meteorologyArray.add(meteorology.getPm10());
            }
            mysqlResults.add(meteorologyArray);
        }
        return mysqlResults;
    }
}
