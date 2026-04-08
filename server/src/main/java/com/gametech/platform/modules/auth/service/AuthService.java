package com.gametech.platform.modules.auth.service;

import com.gametech.platform.modules.auth.dto.LoginRequest;
import com.gametech.platform.modules.auth.dto.LoginResponse;
import com.gametech.platform.modules.auth.dto.RegisterRequest;

public interface AuthService {

    LoginResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
