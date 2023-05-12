package com.weather.service;

import com.weather.entity.UserRequest;
import com.weather.utils.Result;

public interface UserService {
    Result insertUser(UserRequest userRequest);
}
