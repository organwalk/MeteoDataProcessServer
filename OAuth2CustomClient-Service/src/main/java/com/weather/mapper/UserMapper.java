package com.weather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weather.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Insert("insert into user (name, password) values (#{name}, #{password})")
    Integer insertUser(String name, String password);
}
