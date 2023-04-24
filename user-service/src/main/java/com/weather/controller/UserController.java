package com.weather.controller;

import com.weather.service.UserService;

import com.weather.utils.LoginResult;
import com.weather.utils.LogoutResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qx")
public class UserController {
    @Autowired
    UserService service;
    @PostMapping("/login")
    public LoginResult login(@RequestParam String name, @RequestParam String password){
        return service.getUserByNameAndPassword(name,password);
    }
    @PostMapping("/logout")
    public LogoutResult logout(@RequestParam String id){
        return service.logoutById(Integer.parseInt(id));
    }
}
