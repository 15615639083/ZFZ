package com.gametech.platform.modules.serviceorder.dto;

import javax.validation.constraints.NotBlank;

public class UpdateServiceOrderStatusRequest {

    @NotBlank(message = "status is required")
    private String status;

    private String deliveryNote;
    private String deliveryAttachmentUrl;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
