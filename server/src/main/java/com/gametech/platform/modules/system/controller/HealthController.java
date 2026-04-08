package com.gametech.platform.modules.system.controller;

import com.gametech.platform.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "gametech-platform-server");
        data.put("status", "UP");
        data.put("stage", "mvp-backend");
        return ApiResponse.success(data);
    }
}
