package com.weather.mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SaveToMySQLMapper {
    @Insert("insert into station (station,name) " +
            "SELECT #{station}, #{name} " +
            "WHERE NOT EXISTS(SELECT station FROM station WHERE station = #{station})")
    int insertStation(@Param("station")String station,@Param("name")String name);

    @Insert("insert into station_date (date,station) " +
            "SELECT #{date}, #{station} " +
            "WHERE NOT EXISTS(SELECT date FROM station_date WHERE station = #{station} and date = #{date} )")
    int insertStationDateRange(@Param("date")String date,@Param("station")String station);
}
