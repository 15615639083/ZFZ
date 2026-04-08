package com.gametech.platform.modules.resource.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.modules.resource.dto.ResourceResponse;
import com.gametech.platform.modules.resource.service.ResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/front/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public ApiResponse<List<ResourceResponse>> list() {
        return ApiResponse.success(resourceService.list());
    }

    @GetMapping("/{id}")
    public ApiResponse<ResourceResponse> detail(@PathVariable Long id) {
        return ApiResponse.success(resourceService.detail(id));
    }
}
