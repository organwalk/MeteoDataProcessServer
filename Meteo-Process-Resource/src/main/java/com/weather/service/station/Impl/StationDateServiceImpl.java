package com.weather.service.station.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.weather.entity.StationDate;
import com.weather.mapper.MySQL.station.StationDateMapper;
import com.weather.service.station.StationDateService;
import com.weather.utils.StationDateResult;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .collect(Collectors.toList());
        return !dateList.isEmpty() ? StationDateResult.success(dateList) : StationDateResult.fail();
    }
}
