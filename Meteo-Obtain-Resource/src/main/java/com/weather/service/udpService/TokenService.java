package com.weather.service.udpService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.client.UDPClient;
import com.weather.entity.request.GetToken;
import com.weather.entity.request.VoidToken;
import com.weather.mapper.TokenMapper;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {
    private final UDPClient udpClient;

    @Resource
    private TokenMapper tokenMapper;

    public void getToken() throws Exception {
        int code = 1;
        String name = "user";
        String password = "123456";

        GetToken getToken = new GetToken(code,name,password);
        ObjectMapper mapper = new ObjectMapper();
        String getTokenRequest = mapper.writeValueAsString(getToken);
        String token = tokenMapper.getToken("token:asdfghjklzxcvbnm");
        if(token != null){
            udpClient.send(getTokenRequest);
        }else {
            System.out.println("token为空!");
        }

    }

    public void voidToken() throws Exception {
        int code = 3;
        String token = tokenMapper.getToken("token:asdfghjklzxcvbnm");

        VoidToken voidToken = new VoidToken(code,token);
        ObjectMapper mapper = new ObjectMapper();
        String voidTokenRequest = mapper.writeValueAsString(voidToken);
        /*String ifTokenExist = String.valueOf(tokenMapper.deleteToken("token:asdfghjklzxcvbnm"));
        if(ifTokenExist == String.valueOf(false)){

        }else {
            System.out.println("redis本无token！");
        }*/
        udpClient.send(voidTokenRequest);
    }
}
