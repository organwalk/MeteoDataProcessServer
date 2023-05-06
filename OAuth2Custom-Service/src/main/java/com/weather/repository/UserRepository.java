package com.weather.repository;


import com.weather.entity.Authority;
import com.weather.entity.User;

import java.util.List;

public interface UserRepository {
    User findUserByName(String name);
    List<Authority> getAuthoritiesByUserId(int userID);
}
