package com.weather.mapper.MySQL.station;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weather.entity.Station;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StationMapper extends BaseMapper<Station> {
    @Select("select station from station")
    List<String> getStationList();
    @Select("SELECT MAX(date) AS latest_date " +
            "FROM ${datasource} " +
            "WHERE station = #{station}")
    String meteoDataLatestDate(@Param("datasource") String dataSource,
                               @Param("station") String station);
    @Select("select COUNT(id) from ${datasource} where station = #{station} limit 1")
    Integer havingDataByStationCode(@Param("datasource") String dataSource,
                                    @Param("station") String station);
}
