package com.example.demo.dto;

import com.example.demo.model.Order; // Pour accéder à Order.OrderStatus
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponse {

    private Long id;
    private Long userId;
    private String productDescription;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Order.OrderStatus status; // Utilise l'énumération définie dans Order
    private LocalDateTime orderDate;

    // Constructors
    public OrderResponse() {
    }

    public OrderResponse(Long id, Long userId, String productDescription, Integer quantity, BigDecimal totalPrice, Order.OrderStatus status, LocalDateTime orderDate) {
        this.id = id;
        this.userId = userId;
        this.productDescription = productDescription;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = orderDate;
    }

    // Static factory method for easy conversion from Order entity to OrderResponse DTO
    public static OrderResponse fromEntity(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getUserId(),
            order.getProductDescription(),
            order.getQuantity(),
            order.getTotalPrice(),
            order.getStatus(),
            order.getOrderDate()
        );
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) { // Correction: Removed extra 'void'
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

    public Order.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(Order.OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}