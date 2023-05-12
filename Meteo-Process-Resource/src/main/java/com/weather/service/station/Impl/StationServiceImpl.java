package com.weather.service.station.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weather.entity.Station;
import com.weather.mapper.MySQL.station.StationMapper;
import com.weather.service.station.StationService;
import com.weather.utils.StationResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class StationServiceImpl implements StationService {
    private final StationMapper stationMapper;

    @Override
    public StationResult getStationInfo() {

        List<Map<String, Object>> stationList = stationMapper.selectMaps(new QueryWrapper<Station>().select("station", "name"));
        return !stationList.isEmpty() ? StationResult.success(stationList):StationResult.fail();

    }
}
