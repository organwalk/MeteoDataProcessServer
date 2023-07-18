package com.weather.service;

import com.weather.entity.request.LoginRequest;
import com.weather.entity.respond.LoginRespond;
import com.weather.entity.request.RegisterRequest;
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
            String token = bCryptPasswordEncoder.encode(username) + UUID.randomUUID();
            userRedis.saveToken(username,token);
//            obtainClient.getToken(username);
            return bCryptPasswordEncoder
                    .matches(loginRequest.getPassword(),
                            userMapper.getEncryptedPassword(username))
                    ? LoginRespond.ok(username,token) : LoginRespond.fail();
        }else {
            return LoginRespond.not_found();
        }

    }

    @Override
    public Result insertUser(RegisterRequest registerRequest) {
        return userMapper.insertUser(registerRequest.getUsername(),
                bCryptPasswordEncoder.encode(registerRequest.getPassword())) != 0
                ? Result.success("success") : Result.fail();
    }

    @Override
    public Result logout(String username) {
        userRedis.voidAccessToken(username);
//        obtainClient.voidToken(username)
        return Result.success(true);
    }

    @Override
    public String checkAccessToken(String username) {
        return (String) userRedis.checkAccessToken(username);
    }
}
