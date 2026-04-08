package com.gametech.platform.modules.engineer.dto;

import javax.validation.constraints.NotBlank;

public class ReviewEngineerRequest {

    @NotBlank(message = "status is required")
    private String status;

    private String remark;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
