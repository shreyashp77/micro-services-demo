package com.example.order_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.order_service.dto.OrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderService {

    @Autowired
    private ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void createOrder(OrderRequest orderRequest) {
        // Logic to process the order can be added here
        System.out.println("Order created: " + orderRequest);
        try {
            kafkaTemplate.send("order-created", objectMapper.writeValueAsString(orderRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}