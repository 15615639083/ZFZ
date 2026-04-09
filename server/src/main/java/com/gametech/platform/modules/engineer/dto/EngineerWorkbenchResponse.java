package com.gametech.platform.modules.engineer.dto;

import com.gametech.platform.modules.serviceorder.dto.ServiceOrderResponse;

import java.util.List;

public class EngineerWorkbenchResponse {

    private EngineerProfileResponse profile;
    private int totalAssignedOrders;
    private int processingOrders;
    private int completedOrders;
    private List<ServiceOrderResponse> recentOrders;

    public EngineerProfileResponse getProfile() {
        return profile;
    }

    public void setProfile(EngineerProfileResponse profile) {
        this.profile = profile;
    }

    public int getTotalAssignedOrders() {
        return totalAssignedOrders;
    }

    public void setTotalAssignedOrders(int totalAssignedOrders) {
        this.totalAssignedOrders = totalAssignedOrders;
    }

    public int getProcessingOrders() {
        return processingOrders;
    }

    public void setProcessingOrders(int processingOrders) {
        this.processingOrders = processingOrders;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(int completedOrders) {
        this.completedOrders = completedOrders;
    }

    public List<ServiceOrderResponse> getRecentOrders() {
        return recentOrders;
    }

    public void setRecentOrders(List<ServiceOrderResponse> recentOrders) {
        this.recentOrders = recentOrders;
    }
}
