package com.gametech.platform.modules.user.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.modules.user.dto.AdminUserResponse;
import com.gametech.platform.modules.user.dto.ToggleUserStatusRequest;
import com.gametech.platform.modules.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;
    private final OperatorContext operatorContext;

    public AdminUserController(UserService userService, OperatorContext operatorContext) {
        this.userService = userService;
        this.operatorContext = operatorContext;
    }

    @GetMapping
    public ApiResponse<List<AdminUserResponse>> list() {
        operatorContext.requireRole("admin");
        return ApiResponse.success(userService.listAllUsers());
    }

    @GetMapping("/{id}")
    public ApiResponse<AdminUserResponse> detail(@PathVariable Long id) {
        operatorContext.requireRole("admin");
        return ApiResponse.success(userService.adminUserDetail(id));
    }

    @PostMapping("/{id}/status")
    public ApiResponse<AdminUserResponse> updateStatus(@PathVariable Long id,
                                                       @Validated @RequestBody ToggleUserStatusRequest request) {
        operatorContext.requireRole("admin");
        return ApiResponse.successMessage("user status updated", userService.updateUserStatus(id, request));
    }
}
