package com.weather.mapper.MySQL.station;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weather.entity.StationDate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface StationDateMapper extends BaseMapper<StationDate> {

    @Select("SELECT YEAR(`date`) AS `year`, COUNT(*) AS `total`\n" +
            "FROM `station_date`" +
            "WHERE station = #{station}" +
            "GROUP BY `year`")
    List<Map<String, Object>> getCollectionYear(@Param("station") String station);
    @Select("SELECT LPAD(MONTH(`date`), 2, '0') AS `month`, COUNT(*) AS `total`" +
            "FROM `station_date`" +
            "WHERE station = #{station} AND YEAR(`date`)=#{year}\n" +
            "GROUP BY `month`\n" +
            "HAVING COUNT(*) > 0\n")
    List<Map<String, Object>> getCollectionMonth(@Param("station") String station,@Param("year") String year);
    @Select("SELECT LPAD(DAY(`date`), 2, '0') AS `day`, COUNT(*) AS `total`" +
            "FROM `station_date`" +
            "WHERE station = #{station} AND YEAR(`date`)=#{year} AND MONTH(`date`)=#{month}" +
            "GROUP BY `day`\n" +
            "HAVING COUNT(*) > 0\n")
    List<Map<String, Object>> getCollectionDay(@Param("station") String station,@Param("year") String year,@Param("month")String month);

    @Select("SELECT DATE_FORMAT(`date`, '%Y-%m-%d') AS `day`, COUNT(*) AS `total` FROM ${tableName} " +
            "WHERE DATE_FORMAT(`date`, '%Y-%m') = #{month} GROUP BY `day` ORDER BY `day` DESC")
    List<Map<String, Object>> countByMonth(@Param("tableName") String tableName, @Param("month") String month);
}
