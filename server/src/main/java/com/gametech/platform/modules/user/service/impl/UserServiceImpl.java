package com.gametech.platform.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.modules.user.dto.UserProfileResponse;
import com.gametech.platform.modules.user.entity.User;
import com.gametech.platform.modules.user.mapper.UserMapper;
import com.gametech.platform.modules.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final OperatorContext operatorContext;

    public UserServiceImpl(UserMapper userMapper, OperatorContext operatorContext) {
        this.userMapper = userMapper;
        this.operatorContext = operatorContext;
    }

    @Override
    public UserProfileResponse currentUser() {
        User user = userMapper.selectById(operatorContext.getUserId());
        if (user == null) {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>().last("limit 1"));
        }
        UserProfileResponse response = new UserProfileResponse();
        if (user != null) {
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setPhone(user.getPhone());
            response.setRole(user.getRole());
            response.setEngineer("engineer".equalsIgnoreCase(user.getRole()) || "admin".equalsIgnoreCase(user.getRole()));
            return response;
        }
        response.setId(operatorContext.getUserId());
        response.setUsername("demo_user");
        response.setPhone("13800000000");
        response.setRole("user");
        response.setEngineer(false);
        return response;
    }
}
