package com.gametech.platform.modules.user.dto;

import javax.validation.constraints.NotBlank;

public class ToggleUserStatusRequest {

    @NotBlank(message = "status is required")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
