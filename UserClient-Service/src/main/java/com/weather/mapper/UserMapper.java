package com.weather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weather.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select id from user where username = #{username}")
    Integer getUid(@Param("username") String username);
    @Select("select password from user where id = #{id}")
    String getEncryptedPassword(@Param("id")Integer id);

    @Insert("insert into user (username, password) values (#{username}, #{password})")
    Integer insertUser(String username, String password);
}
