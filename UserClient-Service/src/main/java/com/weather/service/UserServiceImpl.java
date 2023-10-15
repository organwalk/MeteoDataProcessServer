package com.weather.service;

import com.weather.entity.request.LoginRequest;
import com.weather.entity.respond.LoginRespond;
import com.weather.entity.request.RegisterRequest;
import com.weather.mapper.UserMapper;
import com.weather.mapper.redis.UserRedis;
import com.weather.utils.Result;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * 定义用户服务业务接口实现
 * by organwalk 2023-04-02
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserMapper userMapper;
    private final UserRedis userRedis;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 用户认证业务
     * @param loginRequest 登录请求实体
     * @return 根据不同的认证状态返回结果
     *
     * by organwalk 2023-04-02
     */
    @Override
    public LoginRespond authUser(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        // 检查UID是否存在
        Integer uid = userMapper.getUid(username);
        if (uid == null){
            return LoginRespond.not_found();
        }
        // 检查密码是否正确
        String realPassword = userMapper.getEncryptedPassword(uid);
        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), realPassword)){
            return LoginRespond.not_found();
        }
        // 颁发令牌
        String token = bCryptPasswordEncoder.encode(username) + UUID.randomUUID();
        userRedis.saveToken(username,token);
        return LoginRespond.ok(username,token);

    }

    /**
     * 用户注册业务
     * @param registerRequest 注册请求实体
     * @return 根据注册操作状态返回结果
     *
     * by organwalk 2023-04-02
     */
    @Override
    public Result insertUser(RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        Integer uid = userMapper.getUid(username);
        if (uid != null){
            return Result.fail("用户名" + username + "已存在");
        }
        userMapper.insertUser(username, bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        return Result.success("用户" + username + "注册成功");
    }

    /**
     * 用户登出业务
     * @param username 用户名
     * @return 根据登出操作状态返回结果
     */
    @Override
    public Result logout(String username) {
        if (!userRedis.checkUserStatus(username)){
            return Result.fail("用户尚未登录，无法销毁令牌");
        }
        userRedis.voidAccessToken(username);
        return Result.success("已成功销毁用户" + username + "的令牌");
    }

    /**
     * 获取令牌
     * @param username
     * @return 返回令牌内容
     *
     * by organwalk 2023-04-02
     */
    @Override
    public String getAccessToken(String username) {
        return userRedis.getAccessToken(username);
    }
}
