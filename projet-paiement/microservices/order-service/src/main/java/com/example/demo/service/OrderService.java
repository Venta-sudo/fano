package com.example.demo.service;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
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
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderService(
        OrderRepository orderRepository,
        KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

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

        String message =
            "Order created: " +
            savedOrder.getId() +
            " by User: " +
            savedOrder.getUserId();
        kafkaTemplate.send(ORDER_TOPIC, message);
        log.info(
            "Published message to Kafka topic '{}': {}",
            ORDER_TOPIC,
            message
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
}
