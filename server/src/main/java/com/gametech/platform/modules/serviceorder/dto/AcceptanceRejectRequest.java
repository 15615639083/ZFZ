package com.gametech.platform.modules.serviceorder.dto;

import javax.validation.constraints.NotBlank;

public class AcceptanceRejectRequest {
    @NotBlank(message = "reason is required")
    private String reason;
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
