package com.weather.mapper.MySQL.station;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weather.entity.Station;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StationMapper extends BaseMapper<Station> {
    @Select("select valid from user where id = #{id}")
    int getValidById(int id);
}
