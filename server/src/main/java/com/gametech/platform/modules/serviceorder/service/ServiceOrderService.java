package com.gametech.platform.modules.serviceorder.service;

import com.gametech.platform.modules.serviceorder.dto.CreateServiceOrderRequest;
import com.gametech.platform.modules.serviceorder.dto.DispatchServiceOrderRequest;
import com.gametech.platform.modules.serviceorder.dto.DisputeRequest;
import com.gametech.platform.modules.serviceorder.dto.EngineerRejectOrderRequest;
import com.gametech.platform.modules.serviceorder.dto.AcceptanceRejectRequest;
import com.gametech.platform.modules.serviceorder.dto.ServiceOrderResponse;
import com.gametech.platform.modules.serviceorder.dto.UpdateServiceOrderStatusRequest;

import java.util.List;

public interface ServiceOrderService {

    ServiceOrderResponse create(CreateServiceOrderRequest request);

    List<ServiceOrderResponse> listByCurrentUser();

    List<ServiceOrderResponse> listByCurrentUser(String status);

    ServiceOrderResponse detailByCurrentUser(Long orderId);

    ServiceOrderResponse rejectAcceptance(Long orderId, AcceptanceRejectRequest request);

    List<ServiceOrderResponse> listAll();

    List<ServiceOrderResponse> listAll(String status);

    ServiceOrderResponse adminDetail(Long orderId);

    List<ServiceOrderResponse> listByCurrentEngineer();

    List<ServiceOrderResponse> listByCurrentEngineer(String status);

    ServiceOrderResponse detailByCurrentEngineer(Long orderId);

    ServiceOrderResponse updateByCurrentEngineer(Long orderId, UpdateServiceOrderStatusRequest request);

    ServiceOrderResponse rejectByCurrentEngineer(Long orderId, EngineerRejectOrderRequest request);

    ServiceOrderResponse adminUpdateStatus(Long orderId, UpdateServiceOrderStatusRequest request);

    ServiceOrderResponse createDispute(Long orderId, DisputeRequest request);

    ServiceOrderResponse dispatch(Long orderId, DispatchServiceOrderRequest request);

    ServiceOrderResponse pay(Long orderId);

    ServiceOrderResponse confirm(Long orderId);
}
