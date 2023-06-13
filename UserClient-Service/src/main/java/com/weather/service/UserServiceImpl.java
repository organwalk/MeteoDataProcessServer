package com.weather.service;

import com.weather.entity.LoginRequest;
import com.weather.entity.LoginRespond;
import com.weather.entity.UserRequest;
import com.weather.mapper.UserMapper;
import com.weather.utils.Result;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public LoginRespond authUser(LoginRequest loginRequest) {
        if (userMapper.getUid(loginRequest.getUsername()) != null){
            String encryptedPassword = userMapper.getEncryptedPassword(userMapper.getUid(loginRequest.getUsername()));
            Boolean passwordMatch = bCryptPasswordEncoder.matches(loginRequest.getPassword(),encryptedPassword);
            String firstRandom = UUID.randomUUID().toString();
            String token = firstRandom + "ow" + UUID.randomUUID();
            return passwordMatch ? LoginRespond.ok(token) : LoginRespond.fail();
        }else {
            return LoginRespond.not_found();
        }

    }

    @Override
    public Result insertUser(UserRequest userRequest) {
        String encryptedPassword = bCryptPasswordEncoder.encode(userRequest.getPassword());
        int code = userMapper.insertUser(userRequest.getUsername(),encryptedPassword);
        return code!=0 ? Result.success("registration success") : Result.fail();
    }
}
