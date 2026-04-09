package com.gametech.platform.modules.user.service.impl;

import com.gametech.platform.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.modules.user.dto.AdminUserResponse;
import com.gametech.platform.modules.user.dto.ToggleUserStatusRequest;
import com.gametech.platform.modules.user.dto.UserProfileResponse;
import com.gametech.platform.modules.user.dto.UpdateUserProfileRequest;
import com.gametech.platform.modules.user.entity.User;
import com.gametech.platform.modules.user.mapper.UserMapper;
import com.gametech.platform.modules.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            response.setEmail(user.getEmail());
            response.setAvatar(user.getAvatar());
            response.setRealName(user.getRealName());
            response.setRole(user.getRole());
            response.setEngineer("engineer".equalsIgnoreCase(user.getRole()) || "admin".equalsIgnoreCase(user.getRole()));
            return response;
        }
        response.setId(operatorContext.getUserId());
        response.setUsername("demo_user");
        response.setPhone("13800000000");
        response.setEmail("demo_user@example.com");
        response.setRole("user");
        response.setEngineer(false);
        return response;
    }

    @Override
    public UserProfileResponse updateCurrentUser(UpdateUserProfileRequest request) {
        User current = userMapper.selectById(operatorContext.getUserId());
        if (current == null) {
            throw new BusinessException("user not found");
        }
        User byUsername = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername())
                .ne(User::getId, current.getId())
                .last("limit 1"));
        if (byUsername != null) {
            throw new BusinessException("username already exists");
        }
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            User byPhone = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, request.getPhone().trim())
                    .ne(User::getId, current.getId())
                    .last("limit 1"));
            if (byPhone != null) {
                throw new BusinessException("phone already exists");
            }
        }
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            User byEmail = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, request.getEmail().trim())
                    .ne(User::getId, current.getId())
                    .last("limit 1"));
            if (byEmail != null) {
                throw new BusinessException("email already exists");
            }
        }
        current.setUsername(request.getUsername().trim());
        current.setPhone(blankToNull(request.getPhone()));
        current.setEmail(blankToNull(request.getEmail()));
        current.setRealName(blankToNull(request.getRealName()));
        current.setAvatar(blankToNull(request.getAvatar()));
        current.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(current);
        return currentUser();
    }

    @Override
    public List<AdminUserResponse> listAllUsers() {
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().orderByDesc(User::getId));
        List<AdminUserResponse> responses = new ArrayList<>();
        for (User user : users) {
            responses.add(convertAdminUser(user));
        }
        return responses;
    }

    @Override
    public AdminUserResponse adminUserDetail(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("user not found");
        }
        return convertAdminUser(user);
    }

    @Override
    public AdminUserResponse updateUserStatus(Long userId, ToggleUserStatusRequest request) {
        String status = request.getStatus().trim().toLowerCase();
        if (!"active".equals(status) && !"disabled".equals(status)) {
            throw new BusinessException("status must be active or disabled");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("user not found");
        }
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
        return convertAdminUser(user);
    }

    private String blankToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    private AdminUserResponse convertAdminUser(User user) {
        AdminUserResponse response = new AdminUserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setRealName(user.getRealName());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        return response;
    }
}
