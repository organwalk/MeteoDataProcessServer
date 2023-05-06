package com.weather.service;

import com.weather.entity.Authority;
import com.weather.repository.UserRepository;
import com.weather.security.SecurityUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MybatisPlusUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = userRepository.findUserByName(username);
        List<Authority> authorities = userRepository.getAuthoritiesByUserId(u.getId());
        return Optional.ofNullable(u)
                .map(user ->new SecurityUser(user,authorities))
                .orElseThrow(()->new UsernameNotFoundException("Username not fount " + username));
    }
}
