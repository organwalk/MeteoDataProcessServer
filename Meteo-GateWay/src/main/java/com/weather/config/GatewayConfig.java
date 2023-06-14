package com.weather.config;

import com.weather.interceptor.AuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
@AllArgsConstructor
public class GatewayConfig {
    private final AuthenticationFilter authenticationFilter;
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("meteo-process-resource",r -> r.path("/qx/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://meteo-process-resource"))
                .build();
    }
}
