package com.gametech.platform.modules.engineer.controller;

import com.gametech.platform.common.api.ApiResponse;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.modules.engineer.dto.EngineerApplyRequest;
import com.gametech.platform.modules.engineer.dto.EngineerProfileResponse;
import com.gametech.platform.modules.engineer.dto.EngineerWorkbenchResponse;
import com.gametech.platform.modules.engineer.dto.ReviewEngineerRequest;
import com.gametech.platform.modules.serviceorder.dto.ServiceOrderResponse;
import com.gametech.platform.modules.serviceorder.dto.UpdateServiceOrderStatusRequest;
import com.gametech.platform.modules.serviceorder.dto.EngineerRejectOrderRequest;
import com.gametech.platform.modules.serviceorder.service.ServiceOrderService;
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
    private final ServiceOrderService serviceOrderService;

    public EngineerController(EngineerService engineerService, OperatorContext operatorContext,
                              ServiceOrderService serviceOrderService) {
        this.engineerService = engineerService;
        this.operatorContext = operatorContext;
        this.serviceOrderService = serviceOrderService;
    }

    @GetMapping("/api/front/engineers")
    public ApiResponse<List<EngineerProfileResponse>> listApproved() {
        return ApiResponse.success(engineerService.listApproved());
    }

    @GetMapping("/api/front/engineers/{id}")
    public ApiResponse<EngineerProfileResponse> detail(@PathVariable Long id) {
        return ApiResponse.success(engineerService.detail(id));
    }

    @GetMapping("/api/front/engineer/me")
    public ApiResponse<EngineerProfileResponse> current() {
        operatorContext.requireLogin();
        return ApiResponse.success(engineerService.current());
    }

    @GetMapping("/api/front/engineer/workbench")
    public ApiResponse<EngineerWorkbenchResponse> workbench() {
        operatorContext.requireLogin();
        return ApiResponse.success(engineerService.workbench());
    }

    @GetMapping("/api/front/engineer/orders")
    public ApiResponse<List<ServiceOrderResponse>> workOrders(@org.springframework.web.bind.annotation.RequestParam(required = false) String status) {
        operatorContext.requireLogin();
        return ApiResponse.success(serviceOrderService.listByCurrentEngineer(status));
    }

    @GetMapping("/api/front/engineer/orders/{id}")
    public ApiResponse<ServiceOrderResponse> workOrderDetail(@PathVariable Long id) {
        operatorContext.requireLogin();
        return ApiResponse.success(serviceOrderService.detailByCurrentEngineer(id));
    }

    @PostMapping("/api/front/engineer/orders/{id}/status")
    public ApiResponse<ServiceOrderResponse> updateWorkOrder(@PathVariable Long id,
                                                             @Validated @RequestBody UpdateServiceOrderStatusRequest request) {
        operatorContext.requireLogin();
        return ApiResponse.successMessage("engineer order updated", serviceOrderService.updateByCurrentEngineer(id, request));
    }

    @PostMapping("/api/front/engineer/orders/{id}/reject")
    public ApiResponse<ServiceOrderResponse> rejectWorkOrder(@PathVariable Long id,
                                                             @Validated @RequestBody EngineerRejectOrderRequest request) {
        operatorContext.requireLogin();
        return ApiResponse.successMessage("engineer order rejected", serviceOrderService.rejectByCurrentEngineer(id, request));
    }

    @PostMapping("/api/front/engineer/apply")
    public ApiResponse<Map<String, Object>> apply(@Validated @RequestBody EngineerApplyRequest request) {
        operatorContext.requireLogin();
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
