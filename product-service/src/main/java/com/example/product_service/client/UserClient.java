package com.example.product_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.product_service.dto.UserResponse;

@FeignClient(name = "user-service", url = "http://user-service:9002")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserResponse getUserById(@PathVariable("id") String id);

}