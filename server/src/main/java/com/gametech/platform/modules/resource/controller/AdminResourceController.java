package com.gametech.platform.modules.resource.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.modules.resource.dto.CreateResourceRequest;
import com.gametech.platform.modules.resource.dto.ResourceResponse;
import com.gametech.platform.modules.resource.service.ResourceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/resources")
public class AdminResourceController {

    private final ResourceService resourceService;
    private final OperatorContext operatorContext;

    public AdminResourceController(ResourceService resourceService, OperatorContext operatorContext) {
        this.resourceService = resourceService;
        this.operatorContext = operatorContext;
    }

    @GetMapping
    public ApiResponse<List<ResourceResponse>> list() {
        operatorContext.requireRole("admin");
        return ApiResponse.success(resourceService.listAll());
    }

    @PostMapping
    public ApiResponse<ResourceResponse> create(@Validated @RequestBody CreateResourceRequest request) {
        operatorContext.requireRole("admin");
        return ApiResponse.successMessage("resource created", resourceService.create(request));
    }

    @PostMapping("/{id}")
    public ApiResponse<ResourceResponse> update(@PathVariable Long id,
                                                @Validated @RequestBody CreateResourceRequest request) {
        operatorContext.requireRole("admin");
        return ApiResponse.successMessage("resource updated", resourceService.update(id, request));
    }
}
