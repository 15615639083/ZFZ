package com.gametech.platform.modules.serviceorder.service;

import com.gametech.platform.modules.serviceorder.dto.CreateServiceOrderRequest;
import com.gametech.platform.modules.serviceorder.dto.ServiceOrderResponse;

import java.util.List;

public interface ServiceOrderService {

    ServiceOrderResponse create(CreateServiceOrderRequest request);

    List<ServiceOrderResponse> listByCurrentUser();
}
