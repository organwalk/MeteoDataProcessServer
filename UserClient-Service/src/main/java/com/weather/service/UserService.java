package com.weather.service;

import com.weather.entity.request.LoginRequest;
import com.weather.entity.respond.LoginRespond;
import com.weather.entity.request.RegisterRequest;
import com.weather.utils.Result;

public interface UserService {
    LoginRespond authUser(LoginRequest loginRequest);
    Result insertUser(RegisterRequest registerRequest);
    Result logout(String username);
    String checkAccessToken(String username);
}
