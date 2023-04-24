package com.weather.service.station.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weather.entity.Station;
import com.weather.mapper.MySQL.station.StationMapper;
import com.weather.service.station.StationService;
import com.weather.utils.StationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StationServiceImpl implements StationService {
    @Autowired
    StationMapper stationMapper;
    @Override
    public StationResult getStationInfo(int id) {
        int valid = stationMapper.getValidById(id);
        if (valid == 1){
            List<Map<String, Object>> stationList = stationMapper.selectMaps(new QueryWrapper<Station>().select("station", "name"));
            return StationResult.success(stationList);
        }else {
            return StationResult.fail();
        }

    }
}
