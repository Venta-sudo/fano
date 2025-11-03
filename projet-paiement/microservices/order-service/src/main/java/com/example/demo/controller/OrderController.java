package com.example.demo.controller;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid; // Pour activer la validation des DTOs
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("Received request to create order for userId: {}", orderRequest.getUserId());
        OrderResponse createdOrder = orderService.createOrder(orderRequest);
        log.info("Order created with ID: {}", createdOrder.getId());
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    // Vous pourriez ajouter d'autres endpoints ici, par exemple pour GET /api/orders/{id}
    // mais pour la Phase 1.1, la création est la priorité.
}
