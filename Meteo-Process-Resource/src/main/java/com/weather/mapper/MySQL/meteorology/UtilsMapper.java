package com.weather.mapper.MySQL.meteorology;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UtilsMapper {
    @Select("select count(date) from ${tableName} where date=#{date}")
    int checkMeteoDataExist(@Param("tableName") String tableName,@Param("date")String date);
}
