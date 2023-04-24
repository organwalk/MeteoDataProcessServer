package com.weather.service.station.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.weather.entity.StationDate;
import com.weather.mapper.MySQL.station.StationDateMapper;
import com.weather.service.station.StationDateService;
import com.weather.utils.StationDateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StationDateServiceImpl implements StationDateService {
    @Autowired
    StationDateMapper stationDateMapper;
    @Override
    public StationDateResult getStationDateByStationId(int id,String station) {
        int valid = stationDateMapper.getValidById(id);
        if (valid == 1) {
            QueryWrapper<StationDate> queryWrapper = Wrappers.<StationDate>query()
                    .eq("station", station)
                    .select("date");
            List<Map<String, Object>> stationDate = stationDateMapper.selectMaps(queryWrapper);
            List<String> dateList = stationDate.stream()
                    .map(map -> map.get("date").toString())
                    .collect(Collectors.toList());
            return StationDateResult.success(dateList);
        }else {
            return StationDateResult.fail();
        }
    }
}
