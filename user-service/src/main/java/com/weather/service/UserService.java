package com.weather.service;

import com.weather.utils.LoginResult;
import com.weather.utils.LogoutResult;

public interface UserService {
    LoginResult getUserByNameAndPassword(String name, String password);
    LogoutResult logoutById(int id);
}
