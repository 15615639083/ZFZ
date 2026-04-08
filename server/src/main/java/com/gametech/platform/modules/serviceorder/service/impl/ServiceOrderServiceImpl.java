package com.gametech.platform.modules.serviceorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gametech.platform.common.util.IdGenerator;
import com.gametech.platform.modules.serviceorder.dto.CreateServiceOrderRequest;
import com.gametech.platform.modules.serviceorder.dto.ServiceOrderResponse;
import com.gametech.platform.modules.serviceorder.entity.ServiceOrder;
import com.gametech.platform.modules.serviceorder.mapper.ServiceOrderMapper;
import com.gametech.platform.modules.serviceorder.service.ServiceOrderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceOrderServiceImpl implements ServiceOrderService {

    private final ServiceOrderMapper serviceOrderMapper;

    public ServiceOrderServiceImpl(ServiceOrderMapper serviceOrderMapper) {
        this.serviceOrderMapper = serviceOrderMapper;
    }

    @Override
    public ServiceOrderResponse create(CreateServiceOrderRequest request) {
        ServiceOrder order = new ServiceOrder();
        order.setOrderNo(IdGenerator.orderNo("SO"));
        order.setUserId(1L);
        order.setEngineerId(request.getEngineerId());
        order.setServiceTitle(request.getServiceTitle());
        order.setRequirementDesc(request.getRequirementDesc());
        order.setPrice(request.getPrice());
        order.setStatus("pending_dispatch");
        order.setPayStatus("unpaid");
        serviceOrderMapper.insert(order);
        return convert(order);
    }

    @Override
    public List<ServiceOrderResponse> listByCurrentUser() {
        List<ServiceOrder> orders = serviceOrderMapper.selectList(
                new LambdaQueryWrapper<ServiceOrder>()
                        .eq(ServiceOrder::getUserId, 1L)
                        .orderByDesc(ServiceOrder::getId)
        );
        List<ServiceOrderResponse> responses = new ArrayList<>();
        for (ServiceOrder order : orders) {
            responses.add(convert(order));
        }
        return responses;
    }

    private ServiceOrderResponse convert(ServiceOrder order) {
        ServiceOrderResponse response = new ServiceOrderResponse();
        response.setId(order.getId());
        response.setOrderNo(order.getOrderNo());
        response.setUserId(order.getUserId());
        response.setEngineerId(order.getEngineerId());
        response.setServiceTitle(order.getServiceTitle());
        response.setRequirementDesc(order.getRequirementDesc());
        response.setPrice(order.getPrice());
        response.setStatus(order.getStatus());
        response.setPayStatus(order.getPayStatus());
        return response;
    }
}
