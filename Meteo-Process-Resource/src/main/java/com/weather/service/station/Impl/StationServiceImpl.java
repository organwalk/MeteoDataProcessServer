package com.weather.service.station.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.weather.entity.Station;
import com.weather.entity.StationDate;
import com.weather.mapper.MySQL.station.StationDateMapper;
import com.weather.mapper.MySQL.station.StationMapper;
import com.weather.service.station.StationService;
import com.weather.utils.MeteorologyResult;
import com.weather.utils.StationDateResult;
import com.weather.utils.StationResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 定义气象站接口业务实现
 * by organwalk 2023-04-08
 */
@Service
@AllArgsConstructor
public class StationServiceImpl implements StationService {
    private final StationMapper stationMapper;
    private final StationDateMapper stationDateMapper;

    /**
     * 获取气象站列表
     * @return 返回具有气象站编号和名称的列表
     *
     * by organwalk 2023-04-08
     */
    @Override
    public StationResult getStationInfo() {
        Object stationList = stationMapper.selectMaps(
                new QueryWrapper<Station>().select("station", "name")
        );
        if (Objects.isNull(stationList)){
            return StationResult.fail("无法获取气象站列表，请稍后再试");
        }
        return StationResult.success(stationList);
    }

    /**
     * 获取指定气象站有效日期
     * @param station 气象站编号
     * @return 返回具有气象站编号和有效日期的列表
     *
     * by organwalk 2023-04-08
     */
    @Override
    public StationDateResult getStationDateByStationId(String station) {
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

    /**
     * 获取采集日期的年份列表
     * @param station
     * @return
     *
     * by organwalk 2023-04-08
     */
    @Override
    public StationDateResult getCollectionYear(String station) {
        List<Map<String, Object>> collectionYearList = stationDateMapper.getCollectionYear(station);
        return !collectionYearList.isEmpty() ?
                StationDateResult.success(collectionYearList) : StationDateResult.fail("未能获取到有效采集年份");
    }

    /**
     * 获取采集日期的月份列表
     * @param station
     * @param year
     * @return
     *
     * by organwalk 2023-04-08
     */
    @Override
    public StationDateResult getCollectionMonth(String station, String year) {
        return !stationDateMapper.getCollectionMonth(station,year).isEmpty() ?
                StationDateResult.success(stationDateMapper
                        .getCollectionMonth(station,year)) : StationDateResult.fail("未能获取到有效采集月份");
    }

    /**
     * 获取采集日期的天数列表
     * @param station
     * @param year
     * @param month
     * @return
     *
     * by organwalk 2023-04-08
     */
    @Override
    public StationDateResult getCollectionDay(String station, String year,String month) {
        return !stationDateMapper.getCollectionDay(station,year,month).isEmpty() ?
                StationDateResult.success(stationDateMapper
                        .getCollectionDay(station,year,month)) : StationDateResult.fail("未能获取到有效采集天数");
    }

    /**
     * 获取任一月份下每日数据总量
     * @param station
     * @param year
     * @param month
     * @return
     */
    @Override
    public MeteorologyResult getStationDataSum(String station, String year, String month) {
        List<Map<String, Object>>dataSum = stationDateMapper
                .countByMonth(station + "_" + "meteo_data",
                        year + "-" + month);
        return !dataSum.isEmpty() ? MeteorologyResult.success(station,0,dataSum) : MeteorologyResult.fail();
    }
}
