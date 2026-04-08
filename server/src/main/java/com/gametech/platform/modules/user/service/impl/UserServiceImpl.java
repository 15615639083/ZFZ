package com.gametech.platform.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gametech.platform.modules.user.dto.UserProfileResponse;
import com.gametech.platform.modules.user.entity.User;
import com.gametech.platform.modules.user.mapper.UserMapper;
import com.gametech.platform.modules.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserProfileResponse currentUser() {
        User user = userMapper.selectById(1L);
        if (user == null) {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>().last("limit 1"));
        }
        UserProfileResponse response = new UserProfileResponse();
        if (user != null) {
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setPhone(user.getPhone());
            response.setRole(user.getRole());
            response.setEngineer("engineer".equalsIgnoreCase(user.getRole()));
            return response;
        }
        response.setId(1L);
        response.setUsername("demo_user");
        response.setPhone("13800000000");
        response.setRole("user");
        response.setEngineer(false);
        return response;
    }
}
