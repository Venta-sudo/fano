package com.example.demo.service;

import com.example.demo.dto.event.OrderCreatedEvent;
import com.example.demo.dto.event.InvoiceGeneratedEvent;
import com.example.demo.model.Invoice;
import com.example.demo.repository.InvoiceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class InvoiceService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);
    private static final String ORDER_CREATED_TOPIC = "order-created";
    private static final String INVOICE_GENERATED_TOPIC = "invoice-generated";

    private final InvoiceRepository invoiceRepository;
    private final KafkaTemplate<String, InvoiceGeneratedEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public InvoiceService(InvoiceRepository invoiceRepository, KafkaTemplate<String, InvoiceGeneratedEvent> kafkaTemplate, ObjectMapper objectMapper) {
        this.invoiceRepository = invoiceRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = ORDER_CREATED_TOPIC, groupId = "invoice-service-group")
    @Transactional
    public void handleOrderCreatedEvent(String message) {
        log.info("Received OrderCreatedEvent message: {}", message);

        OrderCreatedEvent orderCreatedEvent;
        try {
            orderCreatedEvent = objectMapper.readValue(message, OrderCreatedEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse OrderCreatedEvent JSON: {}", message, e);
            return;
        }

        if (invoiceRepository.existsByOrderId(orderCreatedEvent.getOrderId())) {
            log.warn("Invoice already exists for order ID: {}. Skipping processing.", orderCreatedEvent.getOrderId());
            return;
        }

        Invoice invoice = new Invoice(
            orderCreatedEvent.getOrderId(),
            orderCreatedEvent.getUserId(),
            orderCreatedEvent.getTotalPrice()
        );
        invoice.setGenerationDate(LocalDateTime.now());

        Invoice savedInvoice = invoiceRepository.save(invoice);
        log.info("Invoice generated and saved to DB for Order ID: {} with Invoice ID: {}",
            orderCreatedEvent.getOrderId(), savedInvoice.getId());

        InvoiceGeneratedEvent invoiceGeneratedEvent = new InvoiceGeneratedEvent(
            savedInvoice.getId(),
            savedInvoice.getOrderId(),
            savedInvoice.getUserId(),
            savedInvoice.getAmount(),
            savedInvoice.getGenerationDate()
        );

        kafkaTemplate.send(INVOICE_GENERATED_TOPIC, invoiceGeneratedEvent);
        log.info("Published InvoiceGeneratedEvent to Kafka topic '{}': {}",
            INVOICE_GENERATED_TOPIC, invoiceGeneratedEvent.toString());
    }

    @Transactional(readOnly = true)
    public Invoice getInvoiceByOrderId(Long orderId) {
        return invoiceRepository.findByOrderId(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Invoice not found for Order ID: " + orderId));
    }
}
