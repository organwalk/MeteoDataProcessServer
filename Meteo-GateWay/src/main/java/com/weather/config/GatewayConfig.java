package com.weather.config;

import com.weather.interceptor.AuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 路由网关配置
 * by organwalk 2023-04-02
 */
@Configuration
@EnableDiscoveryClient
@AllArgsConstructor
public class GatewayConfig {
    private final AuthenticationFilter authenticationFilter;

    /**
     * 针对处理服务和分析预测服务使用认证过滤器
     * @param builder
     * @return
     *
     * by organwalk 2023-04-02
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("meteo-process-resource",r -> r.path("/qx/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://meteo-process-resource"))
                .route("meteo-anapredict-resource", r -> r.path("/anapredict/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("http://localhost:9594"))
                .build();
    }
}
