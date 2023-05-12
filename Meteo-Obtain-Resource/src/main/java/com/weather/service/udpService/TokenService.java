package com.weather.service.udpService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.client.UDPClient;
import com.weather.entity.request.GetToken;
import com.weather.entity.request.VoidToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {
    private final UDPClient udpClient;

    public void getToken() throws Exception {
        int code = 1;
        String name = "user";
        String password = "123456";

        GetToken getToken = new GetToken(code,name,password);
        ObjectMapper mapper = new ObjectMapper();
        String getTokenRequest = mapper.writeValueAsString(getToken);
        udpClient.send(getTokenRequest);
    }

    public void voidToken() throws Exception {
        int code = 3;
        String token = "asdfghjklzxcvbnm";

        VoidToken voidToken = new VoidToken(code,token);
        ObjectMapper mapper = new ObjectMapper();
        String voidTokenRequest = mapper.writeValueAsString(voidToken);
        udpClient.send(voidTokenRequest);
    }
}
