package com.example.userservice.event;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductEventConsumer {

    private static final String TOPIC = "product-updated";

    @KafkaListener(topics = TOPIC, groupId = "user-service")
    public void handleProductUpdated(String order) {
        // Can add some logic here
        // for eg, Send email notification logic to user
        System.out.println("Received product update event: " + order);
    }
}