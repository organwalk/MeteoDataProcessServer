package com.weather.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper{

    @Select("select username from user where username = #{username}")
    String getUid(@Param("username") String username);
    @Select("select password from user where username = #{username}")
    String getEncryptedPassword(@Param("username")String username);

    @Insert("insert into user (username, password) values (#{username}, #{password})")
    Integer insertUser(String username, String password);
}
