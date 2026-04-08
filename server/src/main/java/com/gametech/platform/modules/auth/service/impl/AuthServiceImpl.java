package com.gametech.platform.modules.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gametech.platform.common.exception.BusinessException;
import com.gametech.platform.common.util.IdGenerator;
import com.gametech.platform.modules.auth.dto.LoginRequest;
import com.gametech.platform.modules.auth.dto.LoginResponse;
import com.gametech.platform.modules.auth.dto.RegisterRequest;
import com.gametech.platform.modules.auth.service.AuthService;
import com.gametech.platform.modules.user.entity.User;
import com.gametech.platform.modules.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    public AuthServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        ensureUnique(request.getUsername(), request.getPhone(), request.getEmail());

        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPhone(blankToNull(request.getPhone()));
        user.setEmail(blankToNull(request.getEmail()));
        user.setPasswordHash(request.getPassword());
        user.setRole("user");
        user.setStatus("active");
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userMapper.insert(user);
        return toLoginResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getAccount())
                .or()
                .eq(User::getPhone, request.getAccount())
                .or()
                .eq(User::getEmail, request.getAccount())
                .last("limit 1"));
        if (user == null || !request.getPassword().equals(user.getPasswordHash())) {
            throw new BusinessException("invalid account or password");
        }
        if (!"active".equalsIgnoreCase(user.getStatus())) {
            throw new BusinessException("account is disabled");
        }
        return toLoginResponse(user);
    }

    private void ensureUnique(String username, String phone, String email) {
        if (userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username).last("limit 1")) != null) {
            throw new BusinessException("username already exists");
        }
        if (phone != null && !phone.trim().isEmpty()
                && userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone.trim()).last("limit 1")) != null) {
            throw new BusinessException("phone already exists");
        }
        if (email != null && !email.trim().isEmpty()
                && userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email.trim()).last("limit 1")) != null) {
            throw new BusinessException("email already exists");
        }
    }

    private LoginResponse toLoginResponse(User user) {
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setToken(IdGenerator.token("gtp_"));
        return response;
    }

    private String blankToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }
}
