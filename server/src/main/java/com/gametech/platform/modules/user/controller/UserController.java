package com.gametech.platform.modules.user.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.modules.user.dto.UserProfileResponse;
import com.gametech.platform.modules.user.dto.UpdateUserProfileRequest;
import com.gametech.platform.modules.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/front/user")
public class UserController {

    private final UserService userService;
    private final OperatorContext operatorContext;

    public UserController(UserService userService, OperatorContext operatorContext) {
        this.userService = userService;
        this.operatorContext = operatorContext;
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> me() {
        operatorContext.requireLogin();
        return ApiResponse.success(userService.currentUser());
    }

    @PostMapping("/me")
    public ApiResponse<UserProfileResponse> updateMe(@Validated @RequestBody UpdateUserProfileRequest request) {
        operatorContext.requireLogin();
        return ApiResponse.successMessage("profile updated", userService.updateCurrentUser(request));
    }
}
