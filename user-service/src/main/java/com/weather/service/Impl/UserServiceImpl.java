package com.weather.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weather.entity.User;
import com.weather.mapper.UserMapper;
import com.weather.service.UserService;
import com.weather.utils.LoginResult;
import com.weather.utils.LogoutResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public LoginResult getUserByNameAndPassword(String name, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("name",name)
                .eq("password",password)
                .select("id"));
        if (user!=null){
            user.setValid(1);
            userMapper.updateById(user);
            return LoginResult.success(user.getId());
        }else {
            return LoginResult.fail();
        }
    }

    @Override
    public LogoutResult logoutById(int id) {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("id",id)
                .select("id"));
        if (user!=null){
            user.setValid(0);
            userMapper.updateById(user);
            return LogoutResult.success();
        }else {
            return LogoutResult.fail();
        }
    }
}
