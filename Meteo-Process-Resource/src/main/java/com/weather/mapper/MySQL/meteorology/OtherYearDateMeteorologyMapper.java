package com.weather.mapper.MySQL.meteorology;

import com.weather.entity.Meteorology;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OtherYearDateMeteorologyMapper {
    @Select("<script>" +
            "SELECT date" +
            "<if test=\"which.contains('1'.toString())\">, temperature</if>" +
            "<if test=\"which.contains('2'.toString())\">, humidity</if>" +
            "<if test=\"which.contains('3'.toString())\">, speed</if>" +
            "<if test=\"which.contains('4'.toString())\">, direction</if>" +
            "<if test=\"which.contains('5'.toString())\">, rain</if>" +
            "<if test=\"which.contains('6'.toString())\">, sunlight</if>" +
            "<if test=\"which.contains('7'.toString())\">, pm25</if>" +
            "<if test=\"which.contains('8'.toString())\">, pm10</if>" +
            " FROM ${datasourceStartDate} WHERE DATE_FORMAT(dateTime, '%Y-%m-%d %H:%i:%s') &gt;= '${startDateTime}' " +
            "  AND DATE_FORMAT(dateTime, '%Y-%m-%d %H:%i:%s') &lt;= '${endDateTime}' " +
            "  AND DATE_FORMAT(dateTime, '%H') = '08'" +
            "  AND DATE_FORMAT(dateTime, '%i') = '00'" +
            "  AND DATE_FORMAT(dateTime, '%s') = '00'" +
            "UNION " +
            "SELECT datetime" +
            "<if test=\"which.contains('1'.toString())\">, temperature</if>" +
            "<if test=\"which.contains('2'.toString())\">, humidity</if>" +
            "<if test=\"which.contains('3'.toString())\">, speed</if>" +
            "<if test=\"which.contains('4'.toString())\">, direction</if>" +
            "<if test=\"which.contains('5'.toString())\">, rain</if>" +
            "<if test=\"which.contains('6'.toString())\">, sunlight</if>" +
            "<if test=\"which.contains('7'.toString())\">, pm25</if>" +
            "<if test=\"which.contains('8'.toString())\">, pm10</if>" +
            " FROM ${datasourceEndDate} WHERE DATE_FORMAT(dateTime, '%Y-%m-%d %H:%i:%s') &gt;= '${startDateTime}' " +
            "  AND DATE_FORMAT(dateTime, '%Y-%m-%d %H:%i:%s') &lt;= '${endDateTime}' " +
            "  AND DATE_FORMAT(dateTime, '%H') = '08'" +
            "  AND DATE_FORMAT(dateTime, '%i') = '00'" +
            "  AND DATE_FORMAT(dateTime, '%s') = '00'" +
            "</script>")
    @Results(value =
            {
                    @Result(column = "date", property = "date"),
                    @Result(column = "temperature", property = "temperature"),
                    @Result(column = "humidity", property = "humidity"),
                    @Result(column = "speed", property = "speed"),
                    @Result(column = "direction", property = "direction"),
                    @Result(column = "rain", property = "rain"),
                    @Result(column = "sunlight", property = "sunlight"),
                    @Result(column = "pm25", property = "pm25"),
                    @Result(column = "pm10", property = "pm10")
            }
    )
    List<Meteorology> selectMeteorologyDateInOtherYear(@Param("datasourceStartDate") String dataSourceStartDate,
                                                       @Param("datasourceEndDate") String dataSourceEndDate,
                                                       @Param("startDateTime") String startDateTime,
                                                       @Param("endDateTime") String endDateTime,
                                                       @Param("which") String which);
}
