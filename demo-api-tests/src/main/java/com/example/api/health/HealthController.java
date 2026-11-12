/*
 * Copyright (c) 2026 Nagarro GmbH. Author: Florin Balint.
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Demo code for training purposes. Distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for details.
 */

package com.example.api.health;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;

/**
 * A simple, human-readable health page. The technical health check of Spring Boot Actuator
 * is available at {@code /actuator/health}.
 */
@RestController
@Tag(name = "Health", description = "Is the API up and running?")
public class HealthController {

    private final Instant startedAt = Instant.now();

    @GetMapping("/api/health")
    @Operation(summary = "Health page", description = "Returns status UP and a short message when the API is running.")
    public Mono<HealthResponse> health() {
        Instant now = Instant.now();
        return Mono.just(new HealthResponse("UP", "Demo API is up and running", now,
                Duration.between(startedAt, now).toSeconds()));
    }

    /** Opening http://localhost:8080/ in the browser redirects to the Swagger UI. */
    @Hidden
    @GetMapping("/")
    public Mono<Void> root(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create("/swagger-ui.html"));
        return response.setComplete();
    }

    /**
     * JSON response of {@code GET /health}.
     */
    public record HealthResponse(String status, String message, Instant timestamp, long uptimeSeconds) {
    }
}
