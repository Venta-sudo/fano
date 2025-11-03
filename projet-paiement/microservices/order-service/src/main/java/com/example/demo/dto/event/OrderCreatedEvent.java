package com.example.demo.dto.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderCreatedEvent {
    private Long orderId;
    private Long userId;
    private String productDescription;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Long orderId, Long userId, String productDescription, Integer quantity, BigDecimal totalPrice, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.productDescription = productDescription;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
               "orderId=" + orderId +
               ", userId=" + userId +
               ", productDescription='" + productDescription + '\'' +
               ", quantity=" + quantity +
               ", totalPrice=" + totalPrice +
               ", orderDate=" + orderDate +
               '}';
    }
}