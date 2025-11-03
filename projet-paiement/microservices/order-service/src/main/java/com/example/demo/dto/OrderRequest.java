package com.example.demo.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotBlank(message = "Product description cannot be empty")
    private String productDescription;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Total price cannot be null")
    @DecimalMin(value = "0.01", message = "Total price must be positive")
    private BigDecimal totalPrice;

    // Constructors
    public OrderRequest() {
    }

    public OrderRequest(Long userId, String productDescription, Integer quantity, BigDecimal totalPrice) {
        this.userId = userId;
        this.productDescription = productDescription;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
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
}