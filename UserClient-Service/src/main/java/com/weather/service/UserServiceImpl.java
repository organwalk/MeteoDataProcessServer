package com.weather.service;

import com.weather.entity.LoginRequest;
import com.weather.entity.LoginRespond;
import com.weather.entity.UserRequest;
import com.weather.mapper.UserMapper;
import com.weather.mapper.redis.UserRedis;
import com.weather.obtainclient.ObtainClient;
import com.weather.utils.Result;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserMapper userMapper;
    private final UserRedis userRedis;
    private final ObtainClient obtainClient;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public LoginRespond authUser(LoginRequest loginRequest) {
        String username = userMapper.getUid(loginRequest.getUsername());
        if ( username != null){
            String encryptedPassword = userMapper.getEncryptedPassword(username);
            Boolean passwordMatch = bCryptPasswordEncoder.matches(loginRequest.getPassword(),encryptedPassword);
            String token = bCryptPasswordEncoder.encode(username) + UUID.randomUUID();
            userRedis.saveToken(username,token);
            return passwordMatch ? LoginRespond.ok(username,token) : LoginRespond.fail();
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

    @Override
    public Result logout(String username) {
        boolean obtain = obtainClient.voidToken(username);
        userRedis.voidAccessToken(username);
        return obtain ? Result.success(true) : Result.fail();
    }

    @Override
    public String checkAccessToken(String username) {
        return (String) userRedis.checkAccessToken(username);
    }
}
