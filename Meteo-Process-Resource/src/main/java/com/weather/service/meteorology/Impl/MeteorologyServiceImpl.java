package com.weather.service.meteorology.Impl;

import com.weather.entity.Meteorology;
import com.weather.mapper.MySQL.meteorology.*;
import com.weather.obtainclient.ObtainClient;
import com.weather.service.meteorology.MeteorologyService;
import com.weather.utils.MeteorologyResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final UtilsMapper utils;
    private final ObtainClient obtainClient;

    @Override
    public MeteorologyResult getMeteorologyByHour(String name, String station, String date, String hour, String which) {
        String dataSource = station + "_weather_" + date.split("-")[0];
        if (utils.checkMeteoDataExist(dataSource, date) != null) {
            List<Meteorology> meteorologyList = hourMapper
                    .selectMeteorologyHour(dataSource,
                            String.format("%s %s:00:00", date, hour),
                            String.format("%s %s:00:00", date, hour),
                            which
                    );
            List<List<String>> SQLResults = SQLResult(meteorologyList, "dateTime");
            return !SQLResults.isEmpty() ? MeteorologyResult.success(station, SQLResults) : MeteorologyResult.fail();
        } else {
            return obtainClient.getData(name, station, date, date) ?
                    getMeteorologyByHour(name, station, date, hour, which) : MeteorologyResult.fail();
        }
    }

    @Override
    public MeteorologyResult getMeteorologyByDay(String name, String station, String date, String which, String type) {
        List<List<String>> SQLResults = new ArrayList<>();
        String dataSource = station + "_weather_" + date.split("-")[0];
        if (utils.checkMeteoDataExist(dataSource, date) != null) {
            if (type.equals("1")) {
                List<Meteorology> meteorologyList = dayMapper
                        .selectMeteorologyDay(dataSource,
                                String.format("%s 00:00:00", date),
                                String.format("%s 23:59:59", date),
                                which
                        );
                SQLResults = SQLResult(meteorologyList, "dateTime");
            } else if (type.equals("2")) {
                List<Meteorology> meteorologyList = dayChartMapper
                        .selectMeteorologyDayToCharts(dataSource,
                                String.format("%s 00:00:00", date),
                                String.format("%s 23:59:59", date),
                                which);
                SQLResults = SQLResult(meteorologyList, "dateTime");
            }
            return !SQLResults.isEmpty() ?
                    MeteorologyResult.success(station, SQLResults) : MeteorologyResult.fail();
        } else {
            return obtainClient.getData(name, station, date, date) ?
                    getMeteorologyByDay(name, station, date, which, type) : MeteorologyResult.fail();
        }
    }

    @Override
    public MeteorologyResult getMeteorologyByDate(String name, String station, String startDate, String endDate, String which) {
        List<List<String>> SQLResults = new ArrayList<>();
        //如果查询日期处于同一年
        if (startDate.split("-")[0].equals(endDate.split("-")[0])) {
            String dataSource = station + "_weather_" + startDate.split("-")[0];
            List <List<String>> noExistDateRangeList = getNoExistDateRangeList(dataSource,startDate,endDate);
            if (!noExistDateRangeList.isEmpty()){
                for (List<String> noExistDateList : noExistDateRangeList){
                    return obtainClient.getData(name,station,noExistDateList.get(0),noExistDateList.get(1)) ?
                            getMeteorologyByDate(name, station, startDate, endDate, which) : MeteorologyResult.fail();
                }
            }else {
                List<Meteorology> meteorologyList;
                if (startDate.equals(endDate)){
                    meteorologyList = dateMapper
                            .selectMeteorologyDate(dataSource, startDate, String.valueOf(LocalDate.parse(endDate).plusDays(1)), which);
                }else {
                    meteorologyList = dateMapper
                            .selectMeteorologyDate(dataSource, startDate, endDate, which);
                }
                SQLResults = SQLResult(meteorologyList, "date");
            }
        } else {
            //如果查询日期不处于同一年
            String dataSourceStartDate = station + "_weather_" + startDate.split("-")[0];
            String dataSourceEndDate = station + "_weather_" + endDate.split("-")[0];
            List <List<String>> noExistStartDateRangeList = getNoExistDateRangeList(dataSourceStartDate,startDate,String.format("%s-12-31",startDate.split("-")[0]));
            List <List<String>> noExistEndDateRangeList = getNoExistDateRangeList(dataSourceEndDate,String.format("%s-01-01",endDate.split("-")[0]),endDate);
            if (!noExistStartDateRangeList.isEmpty()){
                for (List<String> noExistStartDate : noExistStartDateRangeList){
                    return obtainClient.getData(name,station, noExistStartDate.get(0), noExistStartDate.get(1)) ?
                            getMeteorologyByDate(name, station, startDate, endDate, which) : MeteorologyResult.fail();
                }
            }else if (!noExistEndDateRangeList.isEmpty()){
                for (List<String> noExistEndDate : noExistEndDateRangeList){
                    return obtainClient.getData(name,station,noExistEndDate.get(0), noExistEndDate.get(1)) ?
                            getMeteorologyByDate(name, station, startDate, endDate, which) : MeteorologyResult.fail();
                }
            }else {
                List<Meteorology> meteorologyList = otherYearDateMapper
                        .selectMeteorologyDateInOtherYear(dataSourceStartDate, dataSourceEndDate, startDate, endDate, which);
                SQLResults = SQLResult(meteorologyList, "date");
            }
        }
        return !SQLResults.isEmpty() ?
                MeteorologyResult.success(station, SQLResults) : MeteorologyResult.fail();
    }

    //4.5 获取指定复杂查询条件的气象数据
    @Override
    public MeteorologyResult getComplexMeteorology(String name,
                                                   String station,
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
        //如果查询日期处于同一年
        if (start_date.split("-")[0].equals(end_date.split("-")[0])) {
            String dataSource = station + "_weather_" + start_date.split("-")[0];
            List <List<String>> noExistDateRangeList = getNoExistDateRangeList(dataSource,start_date,end_date);
            if (!noExistDateRangeList.isEmpty()){
                for (List<String> noExistDateList : noExistDateRangeList){
                    return obtainClient.getData(name,station,noExistDateList.get(0),noExistDateList.get(1)) ?
                            getComplexMeteorology(name, station, start_date, end_date,
                                    start_temperature, end_temperature, start_humidity, end_humidity,
                                    start_speed, end_speed, start_direction, end_direction,
                                    start_rain, end_rain, start_sunlight, end_sunlight,
                                    start_pm25, end_pm25, start_pm10, end_pm10) : MeteorologyResult.fail();
                }
            }else {
                List<Meteorology> meteorologyList;
                if (start_date.equals(end_date)){
                    meteorologyList = complexMapper
                            .selectMeteorologyComplex(dataSource,station,start_date, String.valueOf(LocalDate.parse(end_date).plusDays(1)),
                                    start_temperature, end_temperature, start_humidity, end_humidity,
                                    start_speed, end_speed, start_direction, end_direction,
                                    start_rain, end_rain, start_sunlight, end_sunlight,
                                    start_pm25, end_pm25, start_pm10, end_pm10);
                }else {
                    meteorologyList = complexMapper
                            .selectMeteorologyComplex(dataSource,station,start_date, end_date,
                                    start_temperature, end_temperature, start_humidity, end_humidity,
                                    start_speed, end_speed, start_direction, end_direction,
                                    start_rain, end_rain, start_sunlight, end_sunlight,
                                    start_pm25, end_pm25, start_pm10, end_pm10);
                }
                SQLResults = SQLResult(meteorologyList, "date");
            }
        }
        return !SQLResults.isEmpty() ?
                MeteorologyResult.success(station, SQLResults) : MeteorologyResult.fail();
    }

    /**
     * 匹配需要获取数据的日期范围
     * 有startDate和endDate日期范围，遍历此范围
     * 如果某个日期不在existDate列表里，则将起始日期添加进新的list里
     * 例如: abcdefg，存在d，首先添加a,若遍历至d，即存在的日期，则添加[a,c]字符串列表
     * 实例化新列表保存[a,c]至总列表，清空不存在日期的列表，然后继续添加e，最后得到[e,g]，添加进总列表
     */
    private List<List<String>> getNoExistDateRangeList(String dataSource, String startDate, String endDate) {
        List<String> notExistDate = new ArrayList<>();
        List<List<String>> allDates = new ArrayList<>();
        String rangeStart = null;
        for (LocalDate date = LocalDate.parse(startDate);
             date.isBefore(LocalDate.parse(endDate).plusDays(1));
             date = date.plusDays(1)) {
            if (!utils.
                    checkDateRangeMeteoDataExist(dataSource, startDate, endDate)
                    .contains(date.toString())) {
                if (rangeStart == null) {
                    rangeStart = String.valueOf(date);
                }
            } else {
                if (rangeStart != null) {
                    notExistDate.add(rangeStart);
                    notExistDate.add(date.minusDays(1).toString());
                    allDates.add(new ArrayList<>(notExistDate));
                    notExistDate.clear();
                    rangeStart = null;
                }
            }
        }
        if (rangeStart != null) {
            notExistDate.add(rangeStart);
            notExistDate.add(LocalDate.parse(endDate).toString());
            allDates.add(new ArrayList<>(notExistDate));
        }
        return allDates;
    }

    //统一MySQL查询结果
    private List<List<String>> SQLResult(List<Meteorology> meteorologyList, String type) {
        List<List<String>> mysqlResults = new ArrayList<>();
        for (Meteorology value : meteorologyList) {
            List<String> meteorologyArray = new ArrayList<>();
            if (type.equals("dateTime")) {
                if (value.getDatetime() != null) {
                    meteorologyArray.add(value.getDatetime());
                }
            } else if (type.equals("date")) {
                if (value.getDate() != null) {
                    meteorologyArray.add(value.getDate());
                }
            }
            if (value.getTemperature() != null) {
                meteorologyArray.add(value.getTemperature());
            }
            if (value.getHumidity() != null) {
                meteorologyArray.add(value.getHumidity());
            }
            if (value.getSpeed() != null) {
                meteorologyArray.add(value.getSpeed());
            }
            if (value.getDirection() != null) {
                meteorologyArray.add(value.getDirection());
            }
            if (value.getRain() != null) {
                meteorologyArray.add(value.getRain());
            }
            if (value.getSunlight() != null) {
                meteorologyArray.add(value.getSunlight());
            }
            if (value.getPm25() != null) {
                meteorologyArray.add(value.getPm25());
            }
            if (value.getPm10() != null) {
                meteorologyArray.add(value.getPm10());
            }
            mysqlResults.add(meteorologyArray);
        }
        return mysqlResults;
    }
}
