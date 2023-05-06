package com.weather.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationTokenConverter implements Converter<Jwt, JwtAuthenticationToken> {


    @Override
    public JwtAuthenticationToken convert(Jwt source) {

        /**
         * 只有在用户权限为read时才将jwt令牌传至资源服务器进行公钥验证
         * 否则返回异常空对象
         * 在响应上呈现为401状态码
         **/
        List<String> authorities = (List<String>) source.getClaims().get("authorities");
        List<SimpleGrantedAuthority> readAuthorities = authorities.stream()
                .filter(authority -> authority.equals("read"))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        JwtAuthenticationToken authenticationObj =
                new JwtAuthenticationToken(source, readAuthorities);

        return !readAuthorities.isEmpty() ? authenticationObj : null;
    }
}
