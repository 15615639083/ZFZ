package com.gametech.platform.modules.resource.dto;

public class ResourceDownloadLogResponse {

    private Long id;
    private Long resourceId;
    private String resourceTitle;
    private Long userId;
    private String downloadIp;
    private String createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDownloadIp() {
        return downloadIp;
    }

    public void setDownloadIp(String downloadIp) {
        this.downloadIp = downloadIp;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
