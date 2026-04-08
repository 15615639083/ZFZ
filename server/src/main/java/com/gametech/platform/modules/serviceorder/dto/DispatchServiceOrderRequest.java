package com.gametech.platform.modules.serviceorder.dto;

import javax.validation.constraints.NotNull;

public class DispatchServiceOrderRequest {

    @NotNull(message = "engineerId is required")
    private Long engineerId;

    private String dispatchRemark;

    public Long getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(Long engineerId) {
        this.engineerId = engineerId;
    }

    public String getDispatchRemark() {
        return dispatchRemark;
    }

    public void setDispatchRemark(String dispatchRemark) {
        this.dispatchRemark = dispatchRemark;
    }
}
