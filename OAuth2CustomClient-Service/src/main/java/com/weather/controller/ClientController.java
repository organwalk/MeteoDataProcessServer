package com.weather.controller;

import com.weather.entity.UserRequest;
import com.weather.service.UserService;
import com.weather.utils.Result;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ClientController {
    private final UserService userService;

    // 测试该链接以获取code
    // http://localhost:9094/oauth2/authorize?response_type=code&client_id=meteo_client_web&scope=openid&redirect_uri=http://localhost:8080/user/confirm
    //使用POST请求，将新code替换以下code，以获取令牌体
    //http://localhost:9194/oauth2/token?client_id=meteo_client_one&redirect_uri=http://localhost:9294/authorized&grant_type=authorization_code&code=cgbBle6_tKq7BEWtDA3gRBacB_X3MsCN4-XI83kDm-ltgJ4HbJ0ERFAIssrh0g21O9a3M5AuNi2OQ_iIxL2_Gh7vfTiv561J0cBX4lOIQj5Xs3YCYcV3HVxDBB-4bFfC&code_verifier=qPsH306-ZDDaOE8DFzVn05TkN3ZZoVmI_6x4LsVglQI
    //获取access_token 放置于该请求的头部，以获取资源服务
    @GetMapping("/test/hello")
    public String hello(){
        return "hello";
    }
    @PostMapping("/user")
    public Result register(@RequestBody UserRequest userRequest){
        return userService.insertUser(userRequest);
    }
}
