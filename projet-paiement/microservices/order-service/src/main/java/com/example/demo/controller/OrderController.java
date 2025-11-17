package com.example.demo.controller;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.service.OrderService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(
        OrderController.class
    );
    private final OrderService orderService;
    private final MeterRegistry meterRegistry;

    public OrderController(
        OrderService orderService,
        MeterRegistry meterRegistry
    ) {
        this.orderService = orderService;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
        @Valid @RequestBody OrderRequest orderRequest
    ) {
        log.info(
            "Received request to create order for userId: {}",
            orderRequest.getUserId()
        );

        meterRegistry.counter("orders").increment();

        OrderResponse createdOrder = orderService.createOrder(orderRequest);
        log.info("Order created with ID: {}", createdOrder.getId());
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        log.info("Fetching all orders");
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        log.info("Fetching order with id: {}", id);
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
}
}
