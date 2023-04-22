package com.weather.controller;

import com.weather.entity.User;
import com.weather.service.UserService;
import com.weather.utils.LoginResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qx")
public class UserController {
    @Autowired
    UserService service;
    @PostMapping("/login")
    public LoginResult login(@RequestParam String name, @RequestParam String password){
        return service.getUserByNameAndPassword(name,password);
    }
}
