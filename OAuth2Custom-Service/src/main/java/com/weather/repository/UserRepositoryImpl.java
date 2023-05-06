package com.weather.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weather.entity.Authority;
import com.weather.entity.User;
import com.weather.mapper.AuthorityMapper;
import com.weather.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserMapper userMapper;
    private final AuthorityMapper authorityMapper;
    @Override
    public User findUserByName(String name) {
        return userMapper.selectOne(new QueryWrapper<User>()
                .eq("name",name)
                .select("id","name","password")
        );
    }

    @Override
    public List<Authority> getAuthoritiesByUserId(int userID) {
        return authorityMapper.selectList(new QueryWrapper<Authority>()
                .inSql("id","select authority_id from user_authority where user_id = " + userID)
                .select("id","authority"));
    }
}
