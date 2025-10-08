package com.example.product_service.event;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.product_service.client.UserClient;
import com.example.product_service.dto.OrderEvent;
import com.example.product_service.dto.UserResponse;
import com.example.product_service.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class OrderEventConsumer {

    private static final String TOPIC = "order-created";
    private static final String GROUP_ID = "product-service-group";

    @Autowired
    private ObjectMapper objectMapper;

    private final ProductService productService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserClient userClient;

    public OrderEventConsumer(ProductService productService, KafkaTemplate<String, String> kafkaTemplate,
            UserClient userClient) {
        this.productService = productService;
        this.kafkaTemplate = kafkaTemplate;
        this.userClient = userClient;
    }

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void consume(String messageJson) {
        try {
            OrderEvent order = objectMapper.readValue(messageJson, OrderEvent.class);
            System.out.println("Received order create event: " + order);

            productService.updateProductStock(order.productId(), order.quantity());
            System.out.println("Product stock updated for product ID: " + order.productId());

            String email = this.getUserById(order.userId()).email();
            String orderId = UUID.randomUUID().toString();

            String msg = generateJsonMsg(email, orderId);

            kafkaTemplate.send("product-updated", objectMapper.writeValueAsString(msg));
            System.out.println("Sent product-updated event for order ID: " + orderId + " to email: " + email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to generate JSON message
    private String generateJsonMsg(String email, String orderId) throws JsonProcessingException {
        ObjectNode json = objectMapper.createObjectNode();

        json.put("email", email);
        json.put("order_id", orderId);

        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        return jsonString;
    }

    // Method to fetch user details using Feign client
    public UserResponse getUserById(String userId) {
        return userClient.getUserById(userId);
    }
}