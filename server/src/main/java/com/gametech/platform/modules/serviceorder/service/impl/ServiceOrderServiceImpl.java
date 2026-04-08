package com.gametech.platform.modules.serviceorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gametech.platform.common.exception.BusinessException;
import com.gametech.platform.common.support.OperatorContext;
import com.gametech.platform.common.util.IdGenerator;
import com.gametech.platform.modules.engineer.entity.EngineerProfile;
import com.gametech.platform.modules.engineer.mapper.EngineerProfileMapper;
import com.gametech.platform.modules.serviceorder.dto.CreateServiceOrderRequest;
import com.gametech.platform.modules.serviceorder.dto.DispatchServiceOrderRequest;
import com.gametech.platform.modules.serviceorder.dto.ServiceOrderResponse;
import com.gametech.platform.modules.serviceorder.entity.ServiceOrder;
import com.gametech.platform.modules.serviceorder.mapper.ServiceOrderMapper;
import com.gametech.platform.modules.serviceorder.service.ServiceOrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceOrderServiceImpl implements ServiceOrderService {

    private final ServiceOrderMapper serviceOrderMapper;
    private final EngineerProfileMapper engineerProfileMapper;
    private final OperatorContext operatorContext;

    public ServiceOrderServiceImpl(ServiceOrderMapper serviceOrderMapper, EngineerProfileMapper engineerProfileMapper,
                                   OperatorContext operatorContext) {
        this.serviceOrderMapper = serviceOrderMapper;
        this.engineerProfileMapper = engineerProfileMapper;
        this.operatorContext = operatorContext;
    }

    @Override
    public ServiceOrderResponse create(CreateServiceOrderRequest request) {
        validateEngineer(request.getEngineerId());
        LocalDateTime now = LocalDateTime.now();
        ServiceOrder order = new ServiceOrder();
        order.setOrderNo(IdGenerator.orderNo("SO"));
        order.setUserId(operatorContext.getUserId());
        order.setEngineerId(request.getEngineerId());
        order.setServiceTitle(request.getServiceTitle());
        order.setRequirementDesc(request.getRequirementDesc());
        order.setPrice(request.getPrice());
        order.setStatus("pending_dispatch");
        order.setPayStatus("unpaid");
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        serviceOrderMapper.insert(order);
        return convert(order);
    }

    @Override
    public List<ServiceOrderResponse> listByCurrentUser() {
        List<ServiceOrder> orders = serviceOrderMapper.selectList(
                new LambdaQueryWrapper<ServiceOrder>()
                        .eq(ServiceOrder::getUserId, operatorContext.getUserId())
                        .orderByDesc(ServiceOrder::getId)
        );
        return convert(orders);
    }

    @Override
    public List<ServiceOrderResponse> listAll() {
        return convert(serviceOrderMapper.selectList(new LambdaQueryWrapper<ServiceOrder>().orderByDesc(ServiceOrder::getId)));
    }

    @Override
    public ServiceOrderResponse dispatch(Long orderId, DispatchServiceOrderRequest request) {
        validateEngineer(request.getEngineerId());
        ServiceOrder order = getOrder(orderId);
        if ("completed".equalsIgnoreCase(order.getStatus()) || "cancelled".equalsIgnoreCase(order.getStatus())) {
            throw new BusinessException("order cannot be dispatched");
        }
        order.setEngineerId(request.getEngineerId());
        order.setDispatcherAdminId(operatorContext.getUserId());
        order.setDispatchRemark(request.getDispatchRemark());
        order.setStatus("assigned");
        order.setUpdatedAt(LocalDateTime.now());
        serviceOrderMapper.updateById(order);
        return convert(order);
    }

    @Override
    public ServiceOrderResponse pay(Long orderId) {
        ServiceOrder order = getOwnedOrder(orderId);
        if ("paid".equalsIgnoreCase(order.getPayStatus())) {
            throw new BusinessException("order already paid");
        }
        order.setPayStatus("paid");
        order.setPaidAt(LocalDateTime.now());
        order.setStatus(order.getEngineerId() == null ? "pending_dispatch" : "processing");
        order.setUpdatedAt(LocalDateTime.now());
        serviceOrderMapper.updateById(order);
        return convert(order);
    }

    @Override
    public ServiceOrderResponse confirm(Long orderId) {
        ServiceOrder order = getOwnedOrder(orderId);
        if (!"paid".equalsIgnoreCase(order.getPayStatus())) {
            throw new BusinessException("order is not paid");
        }
        order.setStatus("completed");
        order.setCompletedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        serviceOrderMapper.updateById(order);
        return convert(order);
    }

    private ServiceOrderResponse convert(ServiceOrder order) {
        ServiceOrderResponse response = new ServiceOrderResponse();
        response.setId(order.getId());
        response.setOrderNo(order.getOrderNo());
        response.setUserId(order.getUserId());
        response.setEngineerId(order.getEngineerId());
        response.setDispatcherAdminId(order.getDispatcherAdminId());
        response.setServiceTitle(order.getServiceTitle());
        response.setRequirementDesc(order.getRequirementDesc());
        response.setPrice(order.getPrice());
        response.setStatus(order.getStatus());
        response.setPayStatus(order.getPayStatus());
        response.setDispatchRemark(order.getDispatchRemark());
        return response;
    }

    private List<ServiceOrderResponse> convert(List<ServiceOrder> orders) {
        List<ServiceOrderResponse> responses = new ArrayList<>();
        for (ServiceOrder order : orders) {
            responses.add(convert(order));
        }
        return responses;
    }

    private void validateEngineer(Long engineerId) {
        EngineerProfile profile = engineerProfileMapper.selectById(engineerId);
        if (profile == null || !"approved".equalsIgnoreCase(profile.getVerificationStatus())) {
            throw new BusinessException("approved engineer not found");
        }
    }

    private ServiceOrder getOwnedOrder(Long orderId) {
        ServiceOrder order = getOrder(orderId);
        if (!operatorContext.getUserId().equals(order.getUserId())) {
            throw new BusinessException("permission denied");
        }
        return order;
    }

    private ServiceOrder getOrder(Long orderId) {
        ServiceOrder order = serviceOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("service order not found");
        }
        return order;
    }
}
