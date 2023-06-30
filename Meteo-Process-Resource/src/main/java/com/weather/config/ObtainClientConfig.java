package com.weather.config;

import com.weather.obtainclient.ObtainClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class ObtainClientConfig {
    @Bean
    public ObtainClient obtainClient(){
        WebClient client = WebClient.builder().build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder(
                        WebClientAdapter
                                .forClient(client)).blockTimeout(Duration.ofSeconds(60)
                )
                .build();
        return factory.createClient(ObtainClient.class);
    }
}
