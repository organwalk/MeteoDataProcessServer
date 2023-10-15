package com.weather.config;

import com.weather.userclient.UserClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * 配置注册服务
 * by organwalk 2023-04-02
 */
@Configuration
public class ObtainClientConfig {
    /**
     * 注册用户服务
     * @return
     *
     * by organwalk 2023-04-02
     */
    @Bean
    public UserClient obtainClient(){
        WebClient client = WebClient.builder().build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
        return factory.createClient(UserClient.class);
    }
}
