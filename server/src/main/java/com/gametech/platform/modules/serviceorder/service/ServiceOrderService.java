package com.gametech.platform.modules.serviceorder.service;

import com.gametech.platform.modules.serviceorder.dto.CreateServiceOrderRequest;
import com.gametech.platform.modules.serviceorder.dto.DispatchServiceOrderRequest;
import com.gametech.platform.modules.serviceorder.dto.ServiceOrderResponse;

import java.util.List;

public interface ServiceOrderService {

    ServiceOrderResponse create(CreateServiceOrderRequest request);

    List<ServiceOrderResponse> listByCurrentUser();

    List<ServiceOrderResponse> listAll();

    ServiceOrderResponse dispatch(Long orderId, DispatchServiceOrderRequest request);

    ServiceOrderResponse pay(Long orderId);

    ServiceOrderResponse confirm(Long orderId);
}
