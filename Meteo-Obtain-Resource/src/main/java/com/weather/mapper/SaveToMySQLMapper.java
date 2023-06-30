package com.weather.mapper;
import com.weather.entity.table.MeteoData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    @Insert("<script>" +
            "insert into ${tableName} (station,date,datetime,time,temperature,humidity,speed,direction,rain,sunlight,pm25,pm10) " +
            "values " +
            "<foreach collection='meteoDataList' item='meteoData' separator=','>" +
            "(#{meteoData.station},#{meteoData.date},#{meteoData.datetime},#{meteoData.time},#{meteoData.temperature},#{meteoData.humidity}," +
            "#{meteoData.speed},#{meteoData.direction},#{meteoData.rain},#{meteoData.sunlight},#{meteoData.pm25},#{meteoData.pm10})" +
            "</foreach>" +
            "</script>")
    int insertMeteoData(@Param("tableName") String tableName, @Param("meteoDataList") List<MeteoData> meteoDataList);

    @Select("select date from ${tableName} where date=#{date} LIMIT 1")
    String checkMeteoDataExist(@Param("tableName") String tableName, @Param("date")String date);
}
