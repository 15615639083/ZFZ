package com.gametech.platform.modules.dashboard.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.modules.dashboard.dto.AdminDashboardOverviewResponse;
import com.gametech.platform.modules.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final OperatorContext operatorContext;

    public DashboardController(DashboardService dashboardService, OperatorContext operatorContext) {
        this.dashboardService = dashboardService;
        this.operatorContext = operatorContext;
    }

    @GetMapping("/overview")
    public ApiResponse<AdminDashboardOverviewResponse> overview() {
        operatorContext.requireRole("admin");
        return ApiResponse.success(dashboardService.overview());
    }
}
