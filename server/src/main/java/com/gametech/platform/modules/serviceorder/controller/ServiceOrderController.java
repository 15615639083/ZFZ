package com.gametech.platform.modules.serviceorder.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.modules.serviceorder.dto.CreateServiceOrderRequest;
import com.gametech.platform.modules.serviceorder.dto.ServiceOrderResponse;
import com.gametech.platform.modules.serviceorder.service.ServiceOrderService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/front/service-orders")
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;

    public ServiceOrderController(ServiceOrderService serviceOrderService) {
        this.serviceOrderService = serviceOrderService;
    }

    @PostMapping
    public ApiResponse<ServiceOrderResponse> create(@Validated @RequestBody CreateServiceOrderRequest request) {
        return ApiResponse.successMessage("service order created", serviceOrderService.create(request));
    }

    @GetMapping
    public ApiResponse<List<ServiceOrderResponse>> list() {
        return ApiResponse.success(serviceOrderService.listByCurrentUser());
    }
}
