package com.weather.service.meteorology.Impl;

import com.weather.entity.Meteorology;
import com.weather.mapper.Redis.meteorology.MeteorologyMapper;
import com.weather.mapper.MySQL.meteorology.MeteorologyMySQLMapper;
import com.weather.service.meteorology.MeteorologyService;
import com.weather.utils.MeteorologyResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@AllArgsConstructor
public class MeteorologyServiceImpl implements MeteorologyService {

    private final MeteorologyMapper meteorologyMapper;
    private final MeteorologyMySQLMapper meteorologyMySQLMapper;

    //4.6 获取任一小时的分钟气象信息
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
            if (redisResults != null) {
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
        if (redisResults == null) {
            //System.out.println("查询MySQL");
            String dataSource = station + "_weather_" + date.split("-")[0];
            String startDateTime = date + " " + hour + ":00:00";
            String endDateTime = date + " " + hour + ":59:00";
            List<Meteorology> meteorologyList = meteorologyMySQLMapper.selectMeteorologyHour(dataSource, startDateTime, endDateTime, which);
            SQLResults = SQLResult(meteorologyList, "dateTime");
        }
        // 检查redis数据获取是否有结果
        if (!whichResults.isEmpty()) {
            // 返回成功结果，包含站点和过滤后的数据
            return MeteorologyResult.success(station, whichResults);
        }
        // 检查MySQL数据获取是否有结果
        else if (!SQLResults.isEmpty()) {
            return MeteorologyResult.success(station, SQLResults);
        } else {
            // 返回失败结果
            return MeteorologyResult.fail();
        }
    }

    //4.7 获取任一天的小时气象信息
    @Override
    public MeteorologyResult getMeteorologyByDay(String station, String date, String which, String type) {
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
        if (redisResults != null) {
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
        if (redisResults == null) {
            //System.out.println("查询MySQL");
            String dataSource = station + "_weather_" + date.split("-")[0];
            String startDateTime = date + " " + "00:00:00";
            String endDateTime = date + " " + "23:00:00";
            if (type.equals("1")) {
                List<Meteorology> meteorologyList = meteorologyMySQLMapper.selectMeteorologyDay(dataSource, startDateTime, endDateTime, which);
                SQLResults = SQLResult(meteorologyList, "dateTime");
            } else if (type.equals("2")) {
                List<Meteorology> meteorologyList = meteorologyMySQLMapper.selectMeteorologyDayToCharts(dataSource, startDateTime, endDateTime, which);
                SQLResults = SQLResult(meteorologyList, "dateTime");
            }
        }
        // 检查redis数据获取是否有结果
        if (!whichResults.isEmpty()) {
            // 返回成功结果，包含站点和过滤后的数据
            return MeteorologyResult.success(station, whichResults);
        }
        // 检查MySQL数据获取是否有结果
        else if (!SQLResults.isEmpty()) {
            return MeteorologyResult.success(station, SQLResults);
        } else {
            // 返回失败结果
            return MeteorologyResult.fail();
        }
    }

    //4.8 获取任意时间段以天为单位的气象数据
    @Override
    public MeteorologyResult getMeteorologyByDate(String station, String start_date, String end_date, String which) {
        //用于存储MySQL数据获取结果
        List<List<String>> SQLResults = new ArrayList<>();

        //如果查询日期处于同一年
        if (start_date.split("-")[0].equals(end_date.split("-")[0])) {
            String dataSource = station + "_weather_" + start_date.split("-")[0];
            String startDateTime = start_date + " " + "08:00:00";
            String endDateTime = end_date + " " + "08:00:00";
            List<Meteorology> meteorologyList = meteorologyMySQLMapper.selectMeteorologyDate(dataSource, startDateTime, endDateTime, which);
            SQLResults = SQLResult(meteorologyList, "date");
        } else {
            //如果查询日期不处于同一年
            String dataSourceStartDate = station + "_weather_" + start_date.split("-")[0];
            String dataSourceEndDate = station + "_weather_" + end_date.split("-")[0];
            String startDateTime = start_date + " " + "08:00:00";
            String endDateTime = end_date + " " + "08:00:00";
            List<Meteorology> meteorologyList = meteorologyMySQLMapper.selectMeteorologyDateInOtherYear(dataSourceStartDate, dataSourceEndDate, startDateTime, endDateTime, which);
            SQLResults = SQLResult(meteorologyList, "date");
        }

        // 检查MySQL数据获取是否有结果
        if (!SQLResults.isEmpty()) {
            return MeteorologyResult.success(station, SQLResults);
        } else {
            // 返回失败结果
            return MeteorologyResult.fail();
        }
    }

    //4.9 计算任一时间段内气象数据的协相关矩阵
    @Override
    public MeteorologyResult corrcoefDate(String station, String start_date, String end_date, String correlation) {
        //System.out.println("正在执行相关系数矩阵计算脚本");
        String[] whichArray = correlation.split(",");
        List<Integer> correlationList = new ArrayList<>();
        for (String s : whichArray) {
            correlationList.add(Integer.parseInt(s.trim()));
        }

        List<String> corrcoefCommand = new ArrayList<>();
        //python解释器
        corrcoefCommand.add("C:\\Users\\haruki\\AppData\\Local\\Programs\\Python\\Python310\\python.exe");
        //python脚本
        corrcoefCommand.add("C:\\Users\\haruki\\IdeaProjects\\MeteoDataProcessServer\\Meteo-Process-Resource\\src\\main\\resources\\python\\corrcoef.py");
        corrcoefCommand.add(station);
        corrcoefCommand.add(start_date);
        corrcoefCommand.add(end_date);
        corrcoefCommand.add(correlation);

        // Start Python subprocess
        ProcessBuilder builder = new ProcessBuilder(corrcoefCommand);
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Read output from Python subprocess
        try (InputStream inputStream = process.getInputStream()) {
            StringBuilder strCorrcoefResult = new StringBuilder();
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                strCorrcoefResult.append(new String(buffer, 0, length));
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                // 字符串处理
                String[] lists = strCorrcoefResult.substring(2, strCorrcoefResult.length() - 4).split("\\], \\[");
                // 将字符串型嵌套列表转换为实际嵌套列表
                List<List<String>> corrcoefNestedLists = new ArrayList<>();
                for (String list : lists) {
                    List<String> nestedList = new ArrayList<>();
                    String[] items = list.split(", ");
                    for (String item : items) {
                        nestedList.add(item);
                    }
                    corrcoefNestedLists.add(nestedList);
                }
                //System.out.println(corrcoefNestedLists);
                return MeteorologyResult.success(station, corrcoefNestedLists);
            } else {
                //System.err.println("Python subprocess exited with error code: " + exitCode);
                return MeteorologyResult.fail();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        //用于存储MySQL数据获取结果
        List<List<String>> SQLResults = new ArrayList<>();
        //符合条件检索的结果
        List<List<String>> whichResults = new ArrayList<>();
        //System.out.println("查询MySQL");
        //如果查询日期处于同一年
        if (start_date.split("-")[0].equals(end_date.split("-")[0])) {
            String dataSource = station + "_weather_" + start_date.split("-")[0];
            List<Meteorology> meteorologyList = meteorologyMySQLMapper
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
