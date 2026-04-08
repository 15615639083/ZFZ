package com.gametech.platform.modules.engineer.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.modules.engineer.dto.EngineerApplyRequest;
import com.gametech.platform.modules.engineer.dto.EngineerProfileResponse;
import com.gametech.platform.modules.engineer.dto.ReviewEngineerRequest;
import com.gametech.platform.modules.engineer.service.EngineerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EngineerController {

    private final EngineerService engineerService;
    private final OperatorContext operatorContext;

    public EngineerController(EngineerService engineerService, OperatorContext operatorContext) {
        this.engineerService = engineerService;
        this.operatorContext = operatorContext;
    }

    @GetMapping("/api/front/engineers")
    public ApiResponse<List<EngineerProfileResponse>> listApproved() {
        return ApiResponse.success(engineerService.listApproved());
    }

    @GetMapping("/api/front/engineer/me")
    public ApiResponse<EngineerProfileResponse> current() {
        return ApiResponse.success(engineerService.current());
    }

    @PostMapping("/api/front/engineer/apply")
    public ApiResponse<Map<String, Object>> apply(@Validated @RequestBody EngineerApplyRequest request) {
        Long engineerId = engineerService.apply(request);
        Map<String, Object> data = new HashMap<>();
        data.put("engineerId", engineerId);
        data.put("status", "pending");
        return ApiResponse.successMessage("application submitted", data);
    }

    @GetMapping("/api/admin/engineers")
    public ApiResponse<List<EngineerProfileResponse>> listAll() {
        operatorContext.requireRole("admin");
        return ApiResponse.success(engineerService.listAll());
    }

    @GetMapping("/api/admin/engineers/pending")
    public ApiResponse<List<EngineerProfileResponse>> listPending() {
        operatorContext.requireRole("admin");
        return ApiResponse.success(engineerService.listPending());
    }

    @PostMapping("/api/admin/engineers/{id}/review")
    public ApiResponse<EngineerProfileResponse> review(@PathVariable Long id,
                                                       @Validated @RequestBody ReviewEngineerRequest request) {
        operatorContext.requireRole("admin");
        return ApiResponse.successMessage("review success", engineerService.review(id, request));
    }
}
