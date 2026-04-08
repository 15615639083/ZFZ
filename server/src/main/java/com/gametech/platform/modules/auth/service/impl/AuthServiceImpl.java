package com.gametech.platform.modules.auth.service.impl;

import com.gametech.platform.common.util.IdGenerator;
import com.gametech.platform.modules.auth.dto.LoginRequest;
import com.gametech.platform.modules.auth.dto.LoginResponse;
import com.gametech.platform.modules.auth.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();
        response.setUserId(1L);
        response.setUsername(request.getAccount());
        response.setRole("user");
        response.setToken(IdGenerator.token("gtp_"));
        return response;
    }
}
