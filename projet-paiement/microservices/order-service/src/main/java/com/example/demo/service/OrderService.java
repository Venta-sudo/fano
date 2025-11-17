package com.example.demo.service;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.event.OrderCreatedEvent;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(
        OrderService.class
    );
    private static final String ORDER_TOPIC = "order-created";

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderService(
        OrderRepository orderRepository,
        KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate
    ) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
            .map(this::mapToOrderResponse)
            .collect(Collectors.toList());
    }

    // SUPPRIMEZ CETTE MÉTHODE EN DOUBLE (lignes 40-45)
    /*
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToOrderResponse(order);
    }
    */

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = new Order(
            orderRequest.getUserId(),
            orderRequest.getProductDescription(),
            orderRequest.getQuantity(),
            orderRequest.getTotalPrice()
        );

        Order savedOrder = orderRepository.save(order);
        log.info(
            "Order created and saved to DB with ID: {}",
            savedOrder.getId()
        );

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
            savedOrder.getId(),
            savedOrder.getUserId(),
            savedOrder.getProductDescription(),
            savedOrder.getQuantity(),
            savedOrder.getTotalPrice(),
            savedOrder.getOrderDate()
        );

        kafkaTemplate.send(ORDER_TOPIC, orderCreatedEvent);
        log.info(
            "Published OrderCreatedEvent to Kafka topic '{}': {}",
            ORDER_TOPIC,
            orderCreatedEvent.toString() 
        );

        return OrderResponse.fromEntity(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository
            .findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Order not found with ID: " + id
                )
            );
        return OrderResponse.fromEntity(order);
    }

    // AJOUTEZ CETTE MÉTHODE MANQUANTE
    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.fromEntity(order);
    }
}