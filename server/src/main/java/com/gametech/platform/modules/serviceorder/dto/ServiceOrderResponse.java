package com.gametech.platform.modules.serviceorder.dto;

import java.math.BigDecimal;

public class ServiceOrderResponse {

    private Long id;
    private String orderNo;
    private Long userId;
    private Long engineerId;
    private Long dispatcherAdminId;
    private String serviceTitle;
    private String requirementDesc;
    private BigDecimal price;
    private String status;
    private String payStatus;
    private String dispatchRemark;
    private String deliveryNote;
    private String deliveryAttachmentUrl;
    private String rejectReason;
    private String disputeReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(Long engineerId) {
        this.engineerId = engineerId;
    }

    public Long getDispatcherAdminId() {
        return dispatcherAdminId;
    }

    public void setDispatcherAdminId(Long dispatcherAdminId) {
        this.dispatcherAdminId = dispatcherAdminId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getDispatchRemark() {
        return dispatchRemark;
    }

    public void setDispatchRemark(String dispatchRemark) {
        this.dispatchRemark = dispatchRemark;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public String getDeliveryAttachmentUrl() {
        return deliveryAttachmentUrl;
    }

    public void setDeliveryAttachmentUrl(String deliveryAttachmentUrl) {
        this.deliveryAttachmentUrl = deliveryAttachmentUrl;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getDisputeReason() {
        return disputeReason;
    }

    public void setDisputeReason(String disputeReason) {
        this.disputeReason = disputeReason;
    }
}
