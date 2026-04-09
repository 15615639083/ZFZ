package com.gametech.platform.modules.serviceorder.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.modules.serviceorder.dto.CreateServiceOrderRequest;
import com.gametech.platform.modules.serviceorder.dto.DispatchServiceOrderRequest;
import com.gametech.platform.modules.serviceorder.dto.DisputeRequest;
import com.gametech.platform.modules.serviceorder.dto.AcceptanceRejectRequest;
import com.gametech.platform.modules.serviceorder.dto.ServiceOrderResponse;
import com.gametech.platform.modules.serviceorder.dto.UpdateServiceOrderStatusRequest;
import com.gametech.platform.modules.serviceorder.service.ServiceOrderService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;
    private final OperatorContext operatorContext;

    public ServiceOrderController(ServiceOrderService serviceOrderService, OperatorContext operatorContext) {
        this.serviceOrderService = serviceOrderService;
        this.operatorContext = operatorContext;
    }

    @PostMapping("/api/front/service-orders")
    public ApiResponse<ServiceOrderResponse> create(@Validated @RequestBody CreateServiceOrderRequest request) {
        operatorContext.requireLogin();
        return ApiResponse.successMessage("service order created", serviceOrderService.create(request));
    }

    @GetMapping("/api/front/service-orders")
    public ApiResponse<List<ServiceOrderResponse>> list(@RequestParam(required = false) String status) {
        operatorContext.requireLogin();
        return ApiResponse.success(serviceOrderService.listByCurrentUser(status));
    }

    @GetMapping("/api/front/service-orders/{id}")
    public ApiResponse<ServiceOrderResponse> detail(@PathVariable Long id) {
        operatorContext.requireLogin();
        return ApiResponse.success(serviceOrderService.detailByCurrentUser(id));
    }

    @PostMapping("/api/front/service-orders/{id}/pay")
    public ApiResponse<ServiceOrderResponse> pay(@PathVariable Long id) {
        operatorContext.requireLogin();
        return ApiResponse.successMessage("order paid", serviceOrderService.pay(id));
    }

    @PostMapping("/api/front/service-orders/{id}/confirm")
    public ApiResponse<ServiceOrderResponse> confirm(@PathVariable Long id) {
        operatorContext.requireLogin();
        return ApiResponse.successMessage("order completed", serviceOrderService.confirm(id));
    }

    @PostMapping("/api/front/service-orders/{id}/reject")
    public ApiResponse<ServiceOrderResponse> rejectAcceptance(@PathVariable Long id,
                                                              @Validated @RequestBody AcceptanceRejectRequest request) {
        operatorContext.requireLogin();
        return ApiResponse.successMessage("acceptance rejected", serviceOrderService.rejectAcceptance(id, request));
    }

    @GetMapping("/api/admin/service-orders")
    public ApiResponse<List<ServiceOrderResponse>> adminList(@RequestParam(required = false) String status) {
        operatorContext.requireRole("admin");
        return ApiResponse.success(serviceOrderService.listAll(status));
    }

    @GetMapping("/api/admin/service-orders/{id}")
    public ApiResponse<ServiceOrderResponse> adminDetail(@PathVariable Long id) {
        operatorContext.requireRole("admin");
        return ApiResponse.success(serviceOrderService.adminDetail(id));
    }

    @PostMapping("/api/admin/service-orders/{id}/dispatch")
    public ApiResponse<ServiceOrderResponse> dispatch(@PathVariable Long id,
                                                      @Validated @RequestBody DispatchServiceOrderRequest request) {
        operatorContext.requireRole("admin");
        return ApiResponse.successMessage("order dispatched", serviceOrderService.dispatch(id, request));
    }

    @PostMapping("/api/admin/service-orders/{id}/status")
    public ApiResponse<ServiceOrderResponse> updateStatus(@PathVariable Long id,
                                                          @Validated @RequestBody UpdateServiceOrderStatusRequest request) {
        operatorContext.requireRole("admin");
        return ApiResponse.successMessage("order status updated", serviceOrderService.adminUpdateStatus(id, request));
    }

    @PostMapping("/api/admin/service-orders/{id}/dispute")
    public ApiResponse<ServiceOrderResponse> dispute(@PathVariable Long id,
                                                     @Validated @RequestBody DisputeRequest request) {
        operatorContext.requireRole("admin");
        return ApiResponse.successMessage("order disputed", serviceOrderService.createDispute(id, request));
    }
}
