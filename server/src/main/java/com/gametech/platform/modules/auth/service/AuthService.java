package com.gametech.platform.modules.auth.service;

import com.gametech.platform.modules.auth.dto.LoginRequest;
import com.gametech.platform.modules.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
