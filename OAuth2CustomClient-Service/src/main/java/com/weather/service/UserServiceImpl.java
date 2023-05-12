package com.weather.service;

import com.weather.entity.UserRequest;
import com.weather.mapper.UserMapper;
import com.weather.utils.Result;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public Result insertUser(UserRequest userRequest) {
        String encryptedPassword = bCryptPasswordEncoder.encode(userRequest.getPassword());
        int code = userMapper.insertUser(userRequest.getName(),encryptedPassword);
        return code!=0 ? Result.success("registration success") : Result.fail();
    }
}
