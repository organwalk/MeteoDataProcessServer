package com.weather.controller;

import com.weather.entity.LoginRequest;
import com.weather.entity.LoginRespond;
import com.weather.entity.UserRequest;
import com.weather.obtainclient.ObtainClient;
import com.weather.service.UserService;
import com.weather.utils.Result;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserClientController {
    private final UserService userService;
    private final ObtainClient obtainClient;

    @PostMapping("/login")
    public LoginRespond login(@RequestBody LoginRequest loginRequest){
        return userService.authUser(loginRequest);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserRequest userRequest){
        return userService.insertUser(userRequest);
    }

    @PostMapping("/logout")
    public Result logout(@RequestParam String username){
        return Result.success(obtainClient.voidToken(username));
    }

}
