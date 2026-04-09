package com.gametech.platform.modules.resource.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.modules.resource.dto.ResourceDownloadLogResponse;
import com.gametech.platform.modules.resource.service.ResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/resources")
public class AdminResourceLogController {

    private final ResourceService resourceService;
    private final OperatorContext operatorContext;

    public AdminResourceLogController(ResourceService resourceService, OperatorContext operatorContext) {
        this.resourceService = resourceService;
        this.operatorContext = operatorContext;
    }

    @GetMapping("/download-logs")
    public ApiResponse<List<ResourceDownloadLogResponse>> listDownloadLogs() {
        operatorContext.requireRole("admin");
        return ApiResponse.success(resourceService.listDownloadLogs());
    }
}
