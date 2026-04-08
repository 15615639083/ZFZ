package com.gametech.platform.modules.dashboard.dto;

public class AdminDashboardOverviewResponse {

    private long totalUsers;
    private long totalEngineers;
    private long pendingEngineerReviews;
    private long totalResources;
    private long totalOrders;
    private long pendingDispatchOrders;

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalEngineers() {
        return totalEngineers;
    }

    public void setTotalEngineers(long totalEngineers) {
        this.totalEngineers = totalEngineers;
    }

    public long getPendingEngineerReviews() {
        return pendingEngineerReviews;
    }

    public void setPendingEngineerReviews(long pendingEngineerReviews) {
        this.pendingEngineerReviews = pendingEngineerReviews;
    }

    public long getTotalResources() {
        return totalResources;
    }

    public void setTotalResources(long totalResources) {
        this.totalResources = totalResources;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getPendingDispatchOrders() {
        return pendingDispatchOrders;
    }

    public void setPendingDispatchOrders(long pendingDispatchOrders) {
        this.pendingDispatchOrders = pendingDispatchOrders;
    }
}
