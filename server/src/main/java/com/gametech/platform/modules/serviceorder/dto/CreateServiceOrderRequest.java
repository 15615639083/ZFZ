package com.gametech.platform.modules.serviceorder.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateServiceOrderRequest {

    @NotNull(message = "engineerId is required")
    private Long engineerId;

    @NotBlank(message = "serviceTitle is required")
    private String serviceTitle;

    @NotBlank(message = "requirementDesc is required")
    private String requirementDesc;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "price must be greater than 0")
    private BigDecimal price;

    public Long getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(Long engineerId) {
        this.engineerId = engineerId;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String getRequirementDesc() {
        return requirementDesc;
    }

    public void setRequirementDesc(String requirementDesc) {
        this.requirementDesc = requirementDesc;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
