package com.weather.mapper;

import org.apache.ibatis.annotations.*;

/**
 * 定义用户信息数据源接口
 * by organwalk 2023-04-02
 */
@Mapper
public interface UserMapper{

    /**
     * 根据用户名获取用户ID
     * @param username 用户名
     * @return 整数类型的用户ID
     */
    @Select("select id from user where username = #{username}")
    Integer getUid(@Param("username") String username);
    @Select("select password from user where id = #{uid}")
    String getEncryptedPassword(@Param("uid")Integer uid);

    @Insert("insert into user (username, password) values (#{username}, #{password})")
    Integer insertUser(String username, String password);
}
