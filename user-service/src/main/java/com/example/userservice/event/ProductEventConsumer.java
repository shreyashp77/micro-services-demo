package com.example.userservice.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer for handling product update events.
 * Listens to product-updated topic and processes events related to product
 * changes.
 */
@Service
public class ProductEventConsumer {

    private static final String TOPIC = "product-updated";

    /**
     * Handles product update events from Kafka topic.
     * This method can be extended to implement business logic such as sending email
     * notifications to users.
     * 
     * @param order The product update event data as a string
     */
    @KafkaListener(topics = TOPIC, groupId = "user-service")
    public void handleProductUpdated(String order) {
        // Can add some logic here
        // for eg, Send email notification logic to user
        System.out.println("Received product update event: " + order);
    }
}
