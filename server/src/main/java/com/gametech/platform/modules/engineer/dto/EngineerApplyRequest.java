package com.gametech.platform.modules.engineer.dto;

import javax.validation.constraints.NotBlank;

public class EngineerApplyRequest {

    @NotBlank(message = "realName is required")
    private String realName;

    @NotBlank(message = "skills is required")
    private String skills;

    @NotBlank(message = "serviceTags is required")
    private String serviceTags;

    private String intro;

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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
