package com.gametech.platform.modules.user.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.modules.user.dto.UserProfileResponse;
import com.gametech.platform.modules.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/front/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> me() {
        return ApiResponse.success(userService.currentUser());
    }
}
