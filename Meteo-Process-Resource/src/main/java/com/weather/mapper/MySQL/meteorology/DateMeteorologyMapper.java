package com.weather.mapper.MySQL.meteorology;

import com.weather.entity.Meteorology;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DateMeteorologyMapper {
    @Select("<script>" +
            "SELECT date" +
            "<if test=\"which.contains('1'.toString())\">, ROUND(AVG(COALESCE(temperature, 0)), 2) AS temperature</if>" +
            "<if test=\"which.contains('2'.toString())\">, ROUND(AVG(COALESCE(humidity, 0)), 2) AS humidity</if>" +
            "<if test=\"which.contains('3'.toString())\">, ROUND(AVG(COALESCE(speed, 0)), 2) AS speed</if>" +
            "<if test=\"which.contains('4'.toString())\">, ROUND(AVG(COALESCE(direction, 0)), 2) AS direction</if>" +
            "<if test=\"which.contains('5'.toString())\">, ROUND(AVG(COALESCE(rain, 0)), 2) AS rain</if>" +
            "<if test=\"which.contains('6'.toString())\">, ROUND(AVG(COALESCE(sunlight, 0)), 2) AS sunlight</if>" +
            "<if test=\"which.contains('7'.toString())\">, ROUND(AVG(COALESCE(pm25, 0)), 2) AS pm25</if>" +
            "<if test=\"which.contains('8'.toString())\">, ROUND(AVG(COALESCE(pm10, 0)), 2) AS pm10</if> " +
            "FROM ${datasource} " +
            "WHERE date &gt;= #{startDateTime} AND date &lt; #{endDateTime} " +
            "GROUP BY date limit #{pageSize} offset #{offset}" +
            "</script>")
    @Results(value =
            {
                    @Result(column = "date", property = "date"),
                    @Result(column = "humidity", property = "humidity"),
                    @Result(column = "speed", property = "speed"),
                    @Result(column = "direction", property = "direction"),
                    @Result(column = "rain", property = "rain"),
                    @Result(column = "sunlight", property = "sunlight"),
                    @Result(column = "pm25", property = "pm25"),
                    @Result(column = "pm10", property = "pm10")
            }
    )
    List<Meteorology> selectMeteorologyDate(@Param("datasource") String datasource,
                                            @Param("startDateTime") String startDateTime,
                                            @Param("endDateTime") String endDateTime,
                                            @Param("which") String which,@Param("pageSize") int pageSize, @Param("offset") int offset);

    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM (SELECT date " +
            "<if test=\"which.contains('1'.toString())\">, ROUND(AVG(COALESCE(temperature, 0)), 2) AS temperature</if>" +
            "<if test=\"which.contains('2'.toString())\">, ROUND(AVG(COALESCE(humidity, 0)), 2) AS humidity</if>" +
            "<if test=\"which.contains('3'.toString())\">, ROUND(AVG(COALESCE(speed, 0)), 2) AS speed</if>" +
            "<if test=\"which.contains('4'.toString())\">, ROUND(AVG(COALESCE(direction, 0)), 2) AS direction</if>" +
            "<if test=\"which.contains('5'.toString())\">, ROUND(AVG(COALESCE(rain, 0)), 2) AS rain</if>" +
            "<if test=\"which.contains('6'.toString())\">, ROUND(AVG(COALESCE(sunlight, 0)), 2) AS sunlight</if>" +
            "<if test=\"which.contains('7'.toString())\">, ROUND(AVG(COALESCE(pm25, 0)), 2) AS pm25</if>" +
            "<if test=\"which.contains('8'.toString())\">, ROUND(AVG(COALESCE(pm10, 0)), 2) AS pm10</if> " +
            "FROM ${datasource} " +
            "WHERE date &gt;= #{startDateTime} AND date &lt; #{endDateTime} " +
            "GROUP BY date) AS subquery" +
            "</script>")
    int selectMeteorologyDateCount(@Param("datasource") String datasource,
                             @Param("startDateTime") String startDateTime,
                             @Param("endDateTime") String endDateTime,
                             @Param("which") String which);
}
