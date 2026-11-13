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

package com.example.client;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The CONSUMER: a small client that another application would use to call the calculator API.
 * <p>
 * The consumer contract test checks exactly this class against a Pact mock server. The base URL is
 * passed in, so the same client can talk to the mock server (test) or to the real API (production).
 */
public class CalculatorClient {

    private final WebClient webClient;

    public CalculatorClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    /** {@code GET /api/calculator/add?numbers=..&numbers=..} */
    public CalculationResult add(double... numbers) {
        return webClient.get()
                .uri(builder -> builder.path("/api/calculator/add")
                        .queryParam("numbers", toList(numbers).toArray())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CalculationResult.class)
                .block();
    }

    /** {@code GET /api/calculator/multiply?numbers=..&numbers=..} */
    public CalculationResult multiply(double... numbers) {
        return webClient.get()
                .uri(builder -> builder.path("/api/calculator/multiply")
                        .queryParam("numbers", toList(numbers).toArray())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CalculationResult.class)
                .block();
    }

    /** {@code POST /api/calculator/calculate} with body {@code {"operation": "...", "numbers": [...]}} */
    public CalculationResult calculate(String operation, double... numbers) {
        return webClient.post()
                .uri("/api/calculator/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("operation", operation, "numbers", toList(numbers)))
                .retrieve()
                .bodyToMono(CalculationResult.class)
                .block();
    }

    private static List<Double> toList(double... numbers) {
        return Arrays.stream(numbers).boxed().toList();
    }
}
