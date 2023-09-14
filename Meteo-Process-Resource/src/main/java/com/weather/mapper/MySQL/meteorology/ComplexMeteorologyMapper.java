package com.weather.mapper.MySQL.meteorology;

import com.weather.entity.Meteorology;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ComplexMeteorologyMapper {
    @Select("<script>" +
            "SELECT datetime, temperature, humidity, speed, direction, rain, sunlight, pm25, pm10 " +
            "FROM ${datasource} " +
            "WHERE station = '${station}' " +
            "AND DATE_FORMAT(datetime, '%Y-%m-%d %H:%i:00') &gt;= '${startDate}' " +
            "<if test=\"endDate != null\">AND DATE_FORMAT(datetime, '%Y-%m-%d %H:%i:59') &lt;= '${endDate}' </if>" +
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
            "<if test=\"endPm10 != null\">AND pm10 &lt;= '${endPm10}' </if> limit #{pageSize} offset #{offset}" +
            "</script>")
    @Results(value =
            {
                    @Result(column = "datetime",property = "datetime"),
                    @Result(column = "temperature", property = "temperature"),
                    @Result(column = "humidity",property = "humidity"),
                    @Result(column = "speed",property = "speed"),
                    @Result(column = "direction",property = "direction"),
                    @Result(column = "rain",property = "rain"),
                    @Result(column = "sunlight",property = "sunlight"),
                    @Result(column = "pm25",property = "pm25"),
                    @Result(column = "pm10",property = "pm10")
            }
    )
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
            @Param("endPm10") String endPm10,
            @Param("pageSize") int pageSize, @Param("offset") int offset
    );
    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM ${datasource} " +
            "WHERE station = '${station}' " +
            "AND DATE_FORMAT(datetime, '%Y-%m-%d %H:%i:00') &gt;= '${startDate}' " +
            "<if test=\"endDate != null\">AND DATE_FORMAT(datetime, '%Y-%m-%d %H:%i:59') &lt;= '${endDate}' </if>" +
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
    int selectMeteorologyComplexCount(
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
