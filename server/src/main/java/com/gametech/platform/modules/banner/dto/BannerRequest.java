package com.gametech.platform.modules.banner.dto;

import javax.validation.constraints.NotBlank;

public class BannerRequest {
    private String badgeText;
    @NotBlank(message = "title is required")
    private String title;
    private String subtitle;
    @NotBlank(message = "imageUrl is required")
    private String imageUrl;
    @NotBlank(message = "path is required")
    private String path;
    private String buttonText;
    private String buttonPath;
    private Integer sortOrder;
    private String status;
    public String getBadgeText() { return badgeText; }
    public void setBadgeText(String badgeText) { this.badgeText = badgeText; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getButtonText() { return buttonText; }
    public void setButtonText(String buttonText) { this.buttonText = buttonText; }
    public String getButtonPath() { return buttonPath; }
    public void setButtonPath(String buttonPath) { this.buttonPath = buttonPath; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
