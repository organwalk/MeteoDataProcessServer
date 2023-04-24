package com.weather.service.meteorology.Impl;

import com.weather.entity.Meteorology;
import com.weather.mapper.Redis.meteorology.MeteorologyMapper;
import com.weather.mapper.MySQL.meteorology.MeteorologyMySQLMapper;
import com.weather.service.meteorology.MeteorologyService;
import com.weather.utils.MeteorologyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class MeteorologyServiceImpl implements MeteorologyService {
    @Autowired
    MeteorologyMapper meteorologyMapper;
    @Autowired
    MeteorologyMySQLMapper meteorologyMySQLMapper;


    @Override
    public MeteorologyResult getMeteorologyByHour(String station, String date, String hour, String which) {
        //用于存储Redis数据获取结果
        List<String[]> redisResults = null;
        //用于存储MySQL数据获取结果
        List<List<String>> SQLResults = new ArrayList<>();
        //符合条件检索的结果
        List<List<String>> whichResults = new ArrayList<>();
        // 从Redis中获取任一小时的分钟数据
        for (int i = 0; i < 60; i++) {
            // 计算每分钟的起始时间戳和结束时间戳,东八区，UNIX时间戳
            long startTimestamp = LocalDateTime.of(LocalDate.parse(date), LocalTime.of(Integer.parseInt(hour), i, 0)).toEpochSecond(ZoneOffset.ofHours(8));
            long endTimestamp = LocalDateTime.of(LocalDate.parse(date), LocalTime.of(Integer.parseInt(hour), i, 59)).toEpochSecond(ZoneOffset.ofHours(8));
            // 获取符合对应时间条件的数据
            redisResults = meteorologyMapper.getMeteorologyDataByTime(station + "_data_" + date, startTimestamp, endTimestamp);
            // 获取该小时内60分钟共60个数组
            if (redisResults!=null){
                //System.out.println("查询redis数据");
                for (String[] data : redisResults) {
                    // 判断该数据是否是整点数据（以":00"结尾,即分钟）
                    if (data[0].endsWith(":00")) {
                        // which参数是一个以逗号分隔的字符串，包含要返回的数据的索引。
                        // 如果which中的索引值与data中数组索引相匹配，则将这些值匹配的值添加到selectedValues列表
                        List<String> indexValues = new ArrayList<>(Arrays.asList(which.split(",")));
                        List<String> selectedValues = new ArrayList<>();
                        selectedValues.add(data[0]);
                        for (String indexValue : indexValues) {
                            int index = Integer.parseInt(indexValue.trim());
                            // 如果所需索引的值在该数据集合中，则将其添加到列表中
                            if (index >= 0 && index < data.length) {
                                selectedValues.add(data[index]);
                            }
                        }
                        whichResults.add(selectedValues);
                    }
                }
            }
        }
        //若redis中没有数据，则从MySQL中获取
        if (redisResults==null){
            //System.out.println("查询MySQL");
            String dataSource = station + "_weather_" + date.split("-")[0];
            String startDateTime = date+" "+hour+":00:00";
            String endDateTime = date+" "+hour+":59:00";
            List<Meteorology> meteorologyList = meteorologyMySQLMapper.selectMeteorologyHour(dataSource, startDateTime, endDateTime,which);
            SQLResults = SQLResult(meteorologyList);
        }
        // 检查redis数据获取是否有结果
        if (!whichResults.isEmpty()) {
            // 返回成功结果，包含站点和过滤后的数据
            return MeteorologyResult.success(station, whichResults);
        }
        // 检查MySQL数据获取是否有结果
        else if (!SQLResults.isEmpty()){
            return MeteorologyResult.success(station,SQLResults);
        }
        else {
            // 返回失败结果
            return MeteorologyResult.fail();
        }
    }

    @Override
    public MeteorologyResult getMeteorologyByDay(String station, String date, String which) {
        //用于存储Redis数据获取结果
        List<String[]> redisResults = null;
        //用于存储MySQL数据获取结果
        List<List<String>> SQLResults = new ArrayList<>();
        //符合条件检索的结果
        List<List<String>> whichResults = new ArrayList<>();
        // 计算每天的起始时间戳和结束时间戳,东八区，UNIX时间戳
        long startTimestamp = LocalDateTime.of(LocalDate.parse(date), LocalTime.MIN).toEpochSecond(ZoneOffset.ofHours(8));
        long endTimestamp = LocalDateTime.of(LocalDate.parse(date), LocalTime.of(23, 00, 00)).toEpochSecond(ZoneOffset.ofHours(8));
        // 获取符合对应时间条件的数据
        redisResults = meteorologyMapper.getMeteorologyDataByTime(station + "_data_" + date, startTimestamp, endTimestamp);
        if (redisResults!=null){
            //System.out.println("查询redis数据");
            // 获取任一天的小时数据
            for (String[] data : redisResults) {
                // 判断该数据是否是小时数据（以":00:00"结尾,即小时）
                if (data[0].endsWith(":00:00")) {
                    // which参数是一个以逗号分隔的字符串，包含要返回的数据的索引。
                    // 如果which中的索引值与data中数组索引相匹配，则将这些值匹配的值添加到selectedValues列表
                    List<String> indexValues = new ArrayList<>(Arrays.asList(which.split(",")));
                    List<String> selectedValues = new ArrayList<>();
                    selectedValues.add(data[0]);
                    for (String indexValue : indexValues) {
                        int index = Integer.parseInt(indexValue.trim());
                        // 如果所需索引的值在该数据集合中，则将其添加到列表中
                        if (index >= 0 && index < data.length) {
                            selectedValues.add(data[index]);
                        }
                    }
                    whichResults.add(selectedValues);
                }
            }
        }
        //若redis中没有数据，则从MySQL中获取
        if (redisResults==null){
            //System.out.println("查询MySQL");
            String dataSource = station + "_weather_" + date.split("-")[0];
            String startDateTime = date+" "+"00:00:00";
            String endDateTime = date+" "+"23:00:00";
            List<Meteorology> meteorologyList = meteorologyMySQLMapper.selectMeteorologyDay(dataSource, startDateTime, endDateTime,which);
            SQLResults = SQLResult(meteorologyList);
        }
        // 检查redis数据获取是否有结果
        if (!whichResults.isEmpty()) {
            // 返回成功结果，包含站点和过滤后的数据
            return MeteorologyResult.success(station, whichResults);
        }
        // 检查MySQL数据获取是否有结果
        else if (!SQLResults.isEmpty()){
            return MeteorologyResult.success(station,SQLResults);
        }
        else {
            // 返回失败结果
            return MeteorologyResult.fail();
        }
    }

    @Override
    public MeteorologyResult getMeteorologyByDate(String station, String start_date, String end_date,String which) {
        //用于存储Redis数据获取结果
        List<String[]> redisResults = null;
        //用于存储MySQL数据获取结果
        List<List<String>> SQLResults = new ArrayList<>();
        //符合条件检索的结果
        List<List<String>> whichResults = new ArrayList<>();
        // 计算每天的起始时间戳和结束时间戳,东八区，UNIX时间戳
        long startTimestamp = LocalDateTime.of(LocalDate.parse(start_date), LocalTime.MIN).toEpochSecond(ZoneOffset.ofHours(8));
        long endTimestamp = LocalDateTime.of(LocalDate.parse(end_date), LocalTime.MAX).toEpochSecond(ZoneOffset.ofHours(8));
        // 获取符合对应时间条件的数据
        redisResults = meteorologyMapper.getMeteorologyDataByDate(station,startTimestamp,endTimestamp,start_date,end_date);
        if (!redisResults.isEmpty()){
            //System.out.println("查询redis数据");
            for (String[] data : redisResults) {
                // 取8点的数据作为一天气象数据的基准
                if (data[0].equals("08:00:00")) {
                    // which参数是一个以逗号分隔的字符串，包含要返回的数据的索引。
                    // 如果which中的索引值与data中数组索引相匹配，则将这些值匹配的值添加到selectedValues列表
                    List<String> indexValues = new ArrayList<>(Arrays.asList(which.split(",")));
                    List<String> selectedValues = new ArrayList<>();
                    selectedValues.add(data[0]);
                    for (String indexValue : indexValues) {
                        int index = Integer.parseInt(indexValue.trim());
                        // 如果所需索引的值在该数据集合中，则将其添加到列表中
                        if (index >= 0 && index < data.length) {
                            selectedValues.add(data[index]);
                        }
                    }
                    whichResults.add(selectedValues);
                }
            }
        }else{
            //若redis中没有数据，则从MySQL中获取
            //System.out.println("查询MySQL");
            //如果查询日期处于同一年
            if (start_date.split("-")[0].equals(end_date.split("-")[0])){
                String dataSource = station + "_weather_" + start_date.split("-")[0];
                String startDateTime = start_date+" "+"08:00:00";
                String endDateTime = end_date+" "+"08:00:00";
                List<Meteorology> meteorologyList = meteorologyMySQLMapper.selectMeteorologyDate(dataSource, startDateTime, endDateTime,which);
                SQLResults = SQLResult(meteorologyList);
            }

        }
        // 检查redis数据获取是否有结果
        if (!redisResults.isEmpty()) {
            // 返回成功结果，包含站点和过滤后的数据
            return MeteorologyResult.success(station, whichResults);
        }
        // 检查MySQL数据获取是否有结果
        else if (!SQLResults.isEmpty()){
            return MeteorologyResult.success(station,SQLResults);
        }
        else {
            // 返回失败结果
            return MeteorologyResult.fail();
        }
    }

    private List<List<String>> SQLResult(List<Meteorology> meteorologyList) {
        List<List<String>> mysqlResults = new ArrayList<>();
        for (int i = 0; i < meteorologyList.size(); i++) {
            List<String> meteorologyArray = new ArrayList<>();
            Meteorology meteorology = meteorologyList.get(i);
            if (meteorology.getTime()!=null){
                meteorologyArray.add(meteorology.getTime());
            }
            if (meteorology.getTemperature()!=null){
                meteorologyArray.add(meteorology.getTemperature());
            }
            if (meteorology.getHumidity()!=null){
                meteorologyArray.add(meteorology.getHumidity());
            }
            if (meteorology.getSpeed()!=null){
                meteorologyArray.add(meteorology.getSpeed());
            }
            if (meteorology.getDirection()!= null){
                meteorologyArray.add(meteorology.getDirection());
            }
            if (meteorology.getRain()!=null){
                meteorologyArray.add(meteorology.getRain());
            }
            if (meteorology.getSunlight()!=null){
                meteorologyArray.add(meteorology.getSunlight());
            }
            if (meteorology.getPm25()!=null){
                meteorologyArray.add(meteorology.getPm25());
            }
            if (meteorology.getPm10()!=null){
                meteorologyArray.add(meteorology.getPm10());
            }
            mysqlResults.add(meteorologyArray);
        }
        return mysqlResults;
    }
}
