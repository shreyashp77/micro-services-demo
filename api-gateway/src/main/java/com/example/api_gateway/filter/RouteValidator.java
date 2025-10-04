package com.example.api_gateway.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

    /**
     * List of endpoints that are considered "open" and do not require authentication.
     */
    public static final List<String> openApiEndpoints = List.of(
            "/auth/token"
    );

    /**
     * A predicate that tests if a given request is for a secured endpoint.
     * It returns true if the request URI is NOT in the openApiEndpoints list.
     */
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}