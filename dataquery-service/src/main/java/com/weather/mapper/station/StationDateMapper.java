package com.weather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weather.entity.StationDate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StationDateMapper extends BaseMapper<StationDate> {
    @Select("select valid from user where id = #{id}")
    int getValidById(int id);
}
