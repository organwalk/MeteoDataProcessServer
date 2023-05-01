package com.weather.mapper.MySQL.meteorology;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weather.entity.Meteorology;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MeteorologyMySQLMapper extends BaseMapper<Meteorology> {

    //转义：&gt;为 >  &lt;为 <

    @Select("<script>" +
            "SELECT datetime" +
            "<if test=\"which.contains('1'.toString())\">, temperature</if>" +
            "<if test=\"which.contains('2'.toString())\">, humidity</if>" +
            "<if test=\"which.contains('3'.toString())\">, speed</if>" +
            "<if test=\"which.contains('4'.toString())\">, direction</if>" +
            "<if test=\"which.contains('5'.toString())\">, rain</if>" +
            "<if test=\"which.contains('6'.toString())\">, sunlight</if>" +
            "<if test=\"which.contains('7'.toString())\">, pm25</if>" +
            "<if test=\"which.contains('8'.toString())\">, pm10</if>" +
            " FROM ${datasource} WHERE DATE_FORMAT(dateTime, '%Y-%m-%d %H:%i:00') &gt;= '${startDateTime}' " +
            "  AND DATE_FORMAT(dateTime, '%Y-%m-%d %H:%i:00') &lt;= '${endDateTime}' " +
            "  AND DATE_FORMAT(dateTime, '%s') = '00'" +
            "</script>")
    @Results( id = "SQLResults",
            value =
            {
                @Result(column = "datetime",property = "datetime"),
                @Result(column = "humidity",property = "humidity"),
                @Result(column = "speed",property = "speed"),
                @Result(column = "direction",property = "direction"),
                @Result(column = "rain",property = "rain"),
                @Result(column = "sunlight",property = "sunlight"),
                @Result(column = "pm25",property = "pm25"),
                @Result(column = "pm10",property = "pm10")
            }
    )
    List<Meteorology> selectMeteorologyHour(@Param("datasource") String datasource,
                                         @Param("startDateTime") String startDateTime,
                                         @Param("endDateTime") String endDateTime,
                                         @Param("which") String which);

    @Select("<script>" +
            "SELECT time" +
            "<if test=\"which.contains('1'.toString())\">, temperature</if>" +
            "<if test=\"which.contains('2'.toString())\">, humidity</if>" +
            "<if test=\"which.contains('3'.toString())\">, speed</if>" +
            "<if test=\"which.contains('4'.toString())\">, direction</if>" +
            "<if test=\"which.contains('5'.toString())\">, rain</if>" +
            "<if test=\"which.contains('6'.toString())\">, sunlight</if>" +
            "<if test=\"which.contains('7'.toString())\">, pm25</if>" +
            "<if test=\"which.contains('8'.toString())\">, pm10</if>" +
            " FROM ${datasource} WHERE DATE_FORMAT(dateTime, '%Y-%m-%d %H:%i:%s') &gt;= '${startDateTime}' " +
            "  AND DATE_FORMAT(dateTime, '%Y-%m-%d %H:%i:%s') &lt;= '${endDateTime}' " +
            "  AND DATE_FORMAT(dateTime, '%i') = '00'" +"  AND DATE_FORMAT(dateTime, '%s') = '00'" +
            "</script>")
    @ResultMap(value = "SQLResults")
    List<Meteorology> selectMeteorologyDay(@Param("datasource") String datasource,
                                           @Param("startDateTime") String startDateTime,
                                           @Param("endDateTime") String endDateTime,
                                           @Param("which") String which);

    @Select("<script>" +
            "SELECT datetime" +
            "<if test=\"which.contains('1'.toString())\">, temperature</if>" +
            "<if test=\"which.contains('2'.toString())\">, humidity</if>" +
            "<if test=\"which.contains('3'.toString())\">, speed</if>" +
            "<if test=\"which.contains('4'.toString())\">, direction</if>" +
            "<if test=\"which.contains('5'.toString())\">, rain</if>" +
            "<if test=\"which.contains('6'.toString())\">, sunlight</if>" +
            "<if test=\"which.contains('7'.toString())\">, pm25</if>" +
            "<if test=\"which.contains('8'.toString())\">, pm10</if>" +
            " FROM ${datasource} WHERE DATE_FORMAT(dateTime, '%Y-%m-%d %H:%i:%s') &gt;= '${startDateTime}' " +
            "  AND DATE_FORMAT(dateTime, '%Y-%m-%d %H:%i:%s') &lt;= '${endDateTime}' " +
            "  AND DATE_FORMAT(dateTime, '%H') = '08'" +
            "  AND DATE_FORMAT(dateTime, '%i') = '00'" +
            "  AND DATE_FORMAT(dateTime, '%s') = '00'" +
            "</script>")
    @ResultMap(value = "SQLResults")
    List<Meteorology> selectMeteorologyDate(@Param("datasource") String datasource,
                                           @Param("startDateTime") String startDateTime,
                                           @Param("endDateTime") String endDateTime,
                                           @Param("which") String which);

    @Select("<script>" +
            "SELECT datetime" +
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
            "SELECT time" +
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
    @ResultMap(value = "SQLResults")
    List<Meteorology> selectMeteorologyDateInOtherYear(@Param("datasourceStartDate") String dataSourceStartDate,
                                            @Param("datasourceEndDate") String dataSourceEndDate,
                                            @Param("startDateTime") String startDateTime,
                                            @Param("endDateTime") String endDateTime,
                                            @Param("which") String which);

    @Select("<script>" +
            "SELECT datetime, temperature, humidity, speed, direction, rain, sunlight, pm25, pm10 " +
            "FROM ${datasource} " +
            "WHERE station = '${station}' " +
            "AND DATE_FORMAT(date, '%Y-%m-%d') &gt;= '${startDate}' " +
            "<if test=\"endDate != null\">AND DATE_FORMAT(date, '%Y-%m-%d') &lt;= '${endDate}' </if>" +
            "<if test=\"startTemperature != null\">AND temperature &gt;= '${startTemperature}' </if>" +
            "<if test=\"endTemperature != null\">AND temperature &lt;= '${endTemperature}' </if>" +
            "<if test=\"startHumidity != null\">AND humidity &gt;= '${startHumidity}' </if>" +
            "<if test=\"endHumidity != null\">AND humidity &lt;= '${endHumidity}' </if>" +
            "<if test=\"startSpeed != null\">AND speed &gt;= '${startSpeed}' </if>" +
            "<if test=\"endSpeed != null\">AND speed &lt;= '${endSpeed}' </if>" +
            "<if test=\"startDirection != null\">AND direction &gt;= '${startDirection}' </if>" +
            "<if test=\"endDirection != null\">AND direction &lt;= '${endDirection}' </if>" +
            "<if test=\"startRain != null\">AND rain &gt;= '${startRain}' </if>" +
            "<if test=\"endRain != null\">AND rain &lt;= '${endRain}' </if>" +
            "<if test=\"startSunlight != null\">AND sunlight &gt;= '${startSunlight}' </if>" +
            "<if test=\"endSunlight != null\">AND sunlight &lt;= '${endSunlight}' </if>" +
            "<if test=\"startPm25 != null\">AND pm25 &gt;= '${startPm25}' </if>" +
            "<if test=\"endPm25 != null\">AND pm25 &lt;= '${endPm25}' </if>" +
            "<if test=\"startPm10 != null\">AND pm10 &gt;= '${startPm10}' </if>" +
            "<if test=\"endPm10 != null\">AND pm10 &lt;= '${endPm10}' </if>" +
            "</script>")
    @ResultMap(value = "SQLResults")
    List<Meteorology> selectMeteorologyComplex(
            @Param("datasource") String datasource,
            @Param("station") String station,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("startTemperature") String startTemperature,
            @Param("endTemperature") String endTemperature,
            @Param("startHumidity") String startHumidity,
            @Param("endHumidity") String endHumidity,
            @Param("startSpeed") String startSpeed,
            @Param("endSpeed") String endSpeed,
            @Param("startDirection") String startDirection,
            @Param("endDirection") String endDirection,
            @Param("startRain") String startRain,
            @Param("endRain") String endRain,
            @Param("startSunlight") String startSunlight,
            @Param("endSunlight") String endSunlight,
            @Param("startPm25") String startPm25,
            @Param("endPm25") String endPm25,
            @Param("startPm10") String startPm10,
            @Param("endPm10") String endPm10
    );
}
