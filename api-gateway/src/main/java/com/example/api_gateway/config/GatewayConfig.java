package com.example.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.api_gateway.filter.AuthenticationFilter;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter filter;

    /**
     * Defines the routes for the gateway programmatically and applies the authentication filter.
     * This is an alternative to defining routes in application.properties.
     */
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter.apply(new AuthenticationFilter.Config())))
                        .uri("http://auth-service:9000"))
                        // .uri("http://localhost:9000"))       // use this for local testing

                .route("product-service", r -> r.path("/products/**")
                        .filters(f -> f.filter(filter.apply(new AuthenticationFilter.Config())))
                        .uri("http://product-service:9001"))
                        // .uri("http://localhost:9001"))       // use this for local testing

                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> f.filter(filter.apply(new AuthenticationFilter.Config())))
                        .uri("http://user-service:9002"))
                        // .uri("http://localhost:9002"))       // use this for local testing
                .route("product-service", r -> r.path("/products/**")
                        .filters(f -> f.filter(filter.apply(new AuthenticationFilter.Config())))
                        .uri("http://product-service:9003"))
                        // .uri("http://localhost:9003"))       // use this for local testing
                .build();
    }
}