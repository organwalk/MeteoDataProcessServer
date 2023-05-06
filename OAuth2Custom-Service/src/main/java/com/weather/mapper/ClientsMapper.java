package com.weather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weather.entity.Clients;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClientsMapper extends BaseMapper<Clients> {
}
