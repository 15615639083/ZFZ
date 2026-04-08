package com.gametech.platform.modules.auth.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.modules.auth.dto.LoginRequest;
import com.gametech.platform.modules.auth.dto.LoginResponse;
import com.gametech.platform.modules.auth.dto.RegisterRequest;
import com.gametech.platform.modules.auth.service.AuthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@Validated @RequestBody RegisterRequest request) {
        return ApiResponse.successMessage("register success", authService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }
}
