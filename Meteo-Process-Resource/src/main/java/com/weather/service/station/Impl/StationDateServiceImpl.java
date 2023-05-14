package com.weather.service.station.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.weather.entity.StationDate;
import com.weather.mapper.MySQL.station.StationDateMapper;
import com.weather.service.station.StationDateService;
import com.weather.utils.MeteorologyResult;
import com.weather.utils.StationDateResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StationDateServiceImpl implements StationDateService {
    private final StationDateMapper stationDateMapper;

    @Override
    public StationDateResult getStationDateByStationId(String station) {
        QueryWrapper<StationDate> queryWrapper = Wrappers.<StationDate>query()
                .eq("station", station)
                .select("date");
        List<Map<String, Object>> stationDate = stationDateMapper.selectMaps(queryWrapper);
        List<String> dateList = stationDate.stream()
                .map(map -> map.get("date").toString())
                .map(LocalDate::parse) // 将日期字符串转换为 LocalDate 对象
                .sorted(Comparator.naturalOrder()) // 对日期进行顺序排序
                .map(LocalDate::toString) // 将 LocalDate 对象转换为日期字符串
                .collect(Collectors.toList());
        return !dateList.isEmpty() ? StationDateResult.success(dateList) : StationDateResult.fail();
    }

    @Override
    public StationDateResult getCollectionYear(String station) {
        List<Map<String, Object>> collectionYearList = stationDateMapper.getCollectionYear(station);
        return !collectionYearList.isEmpty() ? StationDateResult.success(collectionYearList) : StationDateResult.fail();
    }

    @Override
    public StationDateResult getCollectionMonth(String station, String year) {
        List<Map<String, Object>> collectionMonthList = stationDateMapper.getCollectionMonth(station,year);
        return !collectionMonthList.isEmpty() ? StationDateResult.success(collectionMonthList) : StationDateResult.fail();
    }

    @Override
    public StationDateResult getCollectionDay(String station, String year,String month) {
        List<Map<String, Object>> collectionDayList = stationDateMapper.getCollectionDay(station,year,month);
        return !collectionDayList.isEmpty() ? StationDateResult.success(collectionDayList) : StationDateResult.fail();
    }

    @Override
    public StationDateResult getStationDataSum(String station, String year, String month) {
        String tableName = station + "_" + "weather" + "_" + year;
        String date = year + "-" + month;
        List<Map<String, Object>>dataSum =  stationDateMapper.countByMonth(tableName,date);
        return !dataSum.isEmpty() ? StationDateResult.success(dataSum) : StationDateResult.fail();
    }
}
