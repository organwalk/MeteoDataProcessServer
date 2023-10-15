package com.weather.controller;

import com.weather.entity.request.LoginRequest;
import com.weather.entity.respond.LoginRespond;
import com.weather.entity.request.RegisterRequest;
import com.weather.service.UserService;
import com.weather.utils.Result;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务接口
 * by organwalk 2023-04-02
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Validated
public class UserClientController {
    private final UserService userService;

    // 登录接口
    @PostMapping("/login")
    public LoginRespond login(@Validated @RequestBody LoginRequest loginRequest){
        return userService.authUser(loginRequest);
    }

    // 注册接口
    @PostMapping("/register")
    public Result register(@Validated @RequestBody RegisterRequest registerRequest){
        return userService.insertUser(registerRequest);
    }

    // 登出接口
    @PostMapping("/logout")
    public Result logout(@RequestParam @NotBlank(message = "username不能为空") String username){
        return userService.logout(username);
    }

    // 获取令牌内部接口
    @GetMapping("/token")
    public String getAccessToken(@RequestParam String username){
        return userService.getAccessToken(username);
    }

}
