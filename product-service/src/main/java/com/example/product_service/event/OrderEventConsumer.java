package com.example.product_service.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.product_service.dto.OrderEvent;
import com.example.product_service.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OrderEventConsumer {

    private static final String TOPIC = "order-created";
    private static final String GROUP_ID = "product-service-group";

    @Autowired
    private ObjectMapper objectMapper;

    private final ProductService productService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderEventConsumer(ProductService productService, KafkaTemplate<String, String> kafkaTemplate) {
        this.productService = productService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void consume(String messageJson) {
        try {
            OrderEvent order = objectMapper.readValue(messageJson, OrderEvent.class);
            System.out.println("Received order create event: " + order);
            productService.updateProductStock(( order.productId()), order.quantity());
            System.out.println("Product stock updated for product ID: " + order.productId());
            kafkaTemplate.send("product-updated", objectMapper.writeValueAsString(order));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}