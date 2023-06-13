package com.weather.service;

import com.weather.entity.LoginRequest;
import com.weather.entity.LoginRespond;
import com.weather.entity.UserRequest;
import com.weather.utils.Result;

public interface UserService {
    LoginRespond authUser(LoginRequest loginRequest);
    Result insertUser(UserRequest userRequest);
}
