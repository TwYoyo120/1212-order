package com.example.ordermanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ShippingInfo")
public class ShippingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient; // 收件人
    private String address; // 地址
    private String status; // 物流狀態

    @Column(name = "order_id", nullable = false)
    private Long orderId; // 關聯的訂單ID

    @Column(name = "tracking_number", nullable = true)
    private String trackingNumber; // 物流追蹤號

    @Column(name = "tracking_image_url")
    private String trackingImageUrl; // 物流單號圖片的 URL

    // Getters 和 Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingImageUrl() {
        return trackingImageUrl;
    }

    public void setTrackingImageUrl(String trackingImageUrl) {
        this.trackingImageUrl = trackingImageUrl;
    }
}