package com.weather.mapper.MySQL.meteorology;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UtilsMapper {
    @Select("select date from ${tableName} where date=#{date} LIMIT 1")
    String checkMeteoDataExist(@Param("tableName") String tableName, @Param("date")String date);

    @Select("SELECT DATE(date) as date " +
            "FROM ${tableName} " +
            "WHERE date BETWEEN #{start} AND #{end} " +
            "GROUP BY DATE(date)")
    List<String> checkDateRangeMeteoDataExist(@Param("tableName")String table,
                                               @Param("start")String start,
                                               @Param("end")String end);
}
