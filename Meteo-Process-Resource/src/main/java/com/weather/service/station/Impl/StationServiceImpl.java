package com.weather.service.station.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.weather.entity.Station;
import com.weather.entity.StationDate;
import com.weather.mapper.MySQL.station.StationDateMapper;
import com.weather.mapper.MySQL.station.StationMapper;
import com.weather.obtainclient.ObtainClient;
import com.weather.service.station.StationService;
import com.weather.utils.StationDateResult;
import com.weather.utils.StationResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StationServiceImpl implements StationService {
    private final StationMapper stationMapper;
    private final StationDateMapper stationDateMapper;
    private final ObtainClient obtainClient;

    @Override
    public StationResult getStationInfo(String authorization) {
//        return obtainClient.getStationCode(authorization) ?
//                StationResult.success(stationMapper
//                        .selectMaps(new QueryWrapper<Station>()
//                                .select("station", "name"))) : StationResult.fail();
        return StationResult.success(stationMapper
                .selectMaps(new QueryWrapper<Station>()
                        .select("station", "name")));
    }

    @Override
    public StationDateResult getStationDateByStationId(String station, String authorization) {
//        return obtainClient.getDateRange(authorization,station) ?
//                StationDateResult.success(stationDateMapper
//                        .selectMaps(Wrappers.<StationDate>query()
//                                .eq("station", station)
//                                .select("date"))
//                        .stream()
//                        .map(map -> map.get("date").toString())
//                        .map(LocalDate::parse)
//                        .sorted(Comparator.naturalOrder())
//                        .map(LocalDate::toString)
//                        .collect(Collectors.toList())) : StationDateResult.fail();
        return StationDateResult.success(stationDateMapper
                .selectMaps(Wrappers.<StationDate>query()
                        .eq("station", station)
                        .select("date"))
                .stream()
                .map(map -> map.get("date").toString())
                .map(LocalDate::parse)
                .sorted(Comparator.naturalOrder())
                .map(LocalDate::toString)
                .collect(Collectors.toList()));
    }

    @Override
    public StationDateResult getCollectionYear(String station) {
        List<Map<String, Object>> collectionYearList = stationDateMapper.getCollectionYear(station);
        return !collectionYearList.isEmpty() ? StationDateResult.success(collectionYearList) : StationDateResult.fail();
    }

    @Override
    public StationDateResult getCollectionMonth(String station, String year) {
        return !stationDateMapper.getCollectionMonth(station,year).isEmpty() ?
                StationDateResult.success(stationDateMapper
                        .getCollectionMonth(station,year)) : StationDateResult.fail();
    }

    @Override
    public StationDateResult getCollectionDay(String station, String year,String month) {
        return !stationDateMapper.getCollectionDay(station,year,month).isEmpty() ?
                StationDateResult.success(stationDateMapper
                        .getCollectionDay(station,year,month)) : StationDateResult.fail();
    }

    @Override
    public StationDateResult getStationDataSum(String station, String year, String month) {
        List<Map<String, Object>>dataSum = stationDateMapper
                .countByMonth(station + "_" + "weather" + "_" + year,
                        year + "-" + month);
        return !dataSum.isEmpty() ? StationDateResult.success(dataSum) : StationDateResult.fail();
    }
}
