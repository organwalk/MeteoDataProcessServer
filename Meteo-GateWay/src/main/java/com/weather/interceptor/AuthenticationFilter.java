package com.weather.interceptor;


import com.weather.userclient.UserClient;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 定义认证过滤器
 * by organwalk 2023-04-02
 */
@Component
@AllArgsConstructor
public class AuthenticationFilter implements GatewayFilter {
    private final UserClient userClient;

    /**
     * 通过用户服务获取令牌与请求头令牌比较，进行放行或拦截判断
     * @param exchange
     * @param chain
     * @return
     *
     * by organwalk 2023.04.02
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String name = exchange.getRequest().getHeaders().getFirst("name");
        String accessToken = exchange.getRequest().getHeaders().getFirst("access_token");
        if (accessToken != null && name != null && accessToken.equals(userClient.getAccessToken(name))) {
            return chain.filter(exchange);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
