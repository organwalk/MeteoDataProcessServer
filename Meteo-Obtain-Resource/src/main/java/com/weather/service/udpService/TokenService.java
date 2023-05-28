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

    public boolean getToken(String userName) throws Exception {
        int code = 1;
        //存储服务所用账号密码
        String name = "root";
        String password = "123456";

        GetToken getToken = new GetToken(code,name,password);
        ObjectMapper mapper = new ObjectMapper();
        String getTokenRequest = mapper.writeValueAsString(getToken);

        if(tokenMapper.getToken(userName) == null){
            System.out.println(getTokenRequest);
            udpClient.send(getTokenRequest);
            return true;
        } else if (tokenMapper.getToken(userName) != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean voidToken(String userName) throws Exception {
        int code = 3;
        String token = tokenMapper.getToken(userName);

        VoidToken voidToken = new VoidToken(code,token);
        ObjectMapper mapper = new ObjectMapper();
        String voidTokenRequest = mapper.writeValueAsString(voidToken);

        udpClient.send(voidTokenRequest);
        System.out.println(voidTokenRequest);

        return tokenMapper.voidToken(userName) != null ? true : false;

    }
}
