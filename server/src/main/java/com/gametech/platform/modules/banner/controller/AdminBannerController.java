package com.gametech.platform.modules.banner.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.modules.banner.dto.BannerRequest;
import com.gametech.platform.modules.banner.dto.BannerResponse;
import com.gametech.platform.modules.banner.service.BannerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/banners")
public class AdminBannerController {
    private final BannerService bannerService;
    private final OperatorContext operatorContext;
    public AdminBannerController(BannerService bannerService, OperatorContext operatorContext) {
        this.bannerService = bannerService;
        this.operatorContext = operatorContext;
    }
    @GetMapping
    public ApiResponse<List<BannerResponse>> list() {
        operatorContext.requireRole("admin");
        return ApiResponse.success(bannerService.listAll());
    }
    @PostMapping
    public ApiResponse<BannerResponse> create(@Validated @RequestBody BannerRequest request) {
        operatorContext.requireRole("admin");
        return ApiResponse.successMessage("banner created", bannerService.create(request));
    }
    @PostMapping("/{id}")
    public ApiResponse<BannerResponse> update(@PathVariable Long id, @Validated @RequestBody BannerRequest request) {
        operatorContext.requireRole("admin");
        return ApiResponse.successMessage("banner updated", bannerService.update(id, request));
    }
}
