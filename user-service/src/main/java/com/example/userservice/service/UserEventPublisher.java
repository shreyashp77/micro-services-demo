// package com.example.userservice.service;

// import com.example.userservice.event.UserCreatedEvent;
// import org.springframework.amqp.core.AmqpTemplate;
// import org.springframework.stereotype.Service;

// @Service
// public class UserEventPublisher {

//     public static final String USER_EXCHANGE = "user.exchange";
//     public static final String USER_CREATED = "user.created";
//     private final AmqpTemplate amqpTemplate;

//     public UserEventPublisher(AmqpTemplate amqpTemplate) {
//         this.amqpTemplate = amqpTemplate;
//     }

//     public void publishUserCreated(UserCreatedEvent event) {
//         amqpTemplate.convertAndSend(USER_EXCHANGE, USER_CREATED, event);
//         System.out.println("UserCreatedEvent published for id: " + event.id());
//     }
// }