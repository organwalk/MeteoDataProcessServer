package com.weather.interceptor;


import com.weather.userclient.UserClient;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthenticationFilter implements GatewayFilter {
    private final UserClient userClient;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String name = exchange.getRequest().getHeaders().getFirst("name");
        String accessToken = exchange.getRequest().getHeaders().getFirst("access_token");
        String checkToken = userClient.checkAccessToken(name);
        if (accessToken != null && accessToken.equals(checkToken)) {
            return chain.filter(exchange);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
