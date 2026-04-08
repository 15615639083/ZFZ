package com.gametech.platform.modules.engineer.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class EngineerApplyRequest {

    @NotBlank(message = "realName is required")
    private String realName;

    @NotBlank(message = "skills is required")
    private String skills;

    @NotBlank(message = "serviceTags is required")
    private String serviceTags;

    private String idCardMask;
    private String certificates;
    private String intro;
    @DecimalMin(value = "0.00", inclusive = true, message = "hourlyPrice must be greater than or equal to 0")
    private BigDecimal hourlyPrice;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getServiceTags() {
        return serviceTags;
    }

    public void setServiceTags(String serviceTags) {
        this.serviceTags = serviceTags;
    }

    public String getIdCardMask() {
        return idCardMask;
    }

    public void setIdCardMask(String idCardMask) {
        this.idCardMask = idCardMask;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public BigDecimal getHourlyPrice() {
        return hourlyPrice;
    }

    public void setHourlyPrice(BigDecimal hourlyPrice) {
        this.hourlyPrice = hourlyPrice;
    }
}
