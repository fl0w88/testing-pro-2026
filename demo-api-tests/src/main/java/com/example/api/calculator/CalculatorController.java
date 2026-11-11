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

package com.example.api.calculator;

import com.example.Calculator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * REST endpoints for the {@link Calculator}.
 * <p>
 * Every method returns a {@link Mono}: the request thread is not blocked, the result is
 * written to the response as soon as it is available.
 */
@RestController
@RequestMapping("/api/calculator")
@Tag(name = "Calculator", description = "Basic arithmetic with double values")
public class CalculatorController {

    private final Calculator calculator;

    public CalculatorController(Calculator calculator) {
        this.calculator = calculator;
    }

    @GetMapping("/add")
    @Operation(summary = "Add numbers", description = "Example: /api/calculator/add?numbers=1&numbers=2 → 3.0. "
            + "At least two numbers are required.")
    public Mono<CalculationResponse> add(
            @Parameter(description = "Numbers to add (at least two)", example = "[1, 2]")
            @RequestParam List<Double> numbers) {
        return calculate(CalculationOperation.ADD, numbers);
    }

    @GetMapping("/subtract")
    @Operation(summary = "Subtract two numbers", description = "Example: /api/calculator/subtract?numbers=5&numbers=3 → 2.0. "
            + "Exactly two numbers are required.")
    public Mono<CalculationResponse> subtract(@RequestParam List<Double> numbers) {
        return calculate(CalculationOperation.SUBTRACT, numbers);
    }

    @GetMapping("/multiply")
    @Operation(summary = "Multiply numbers", description = "At least two numbers are required.")
    public Mono<CalculationResponse> multiply(@RequestParam List<Double> numbers) {
        return calculate(CalculationOperation.MULTIPLY, numbers);
    }

    @GetMapping("/divide")
    @Operation(summary = "Divide numbers from left to right",
            description = "Example: /api/calculator/divide?numbers=12&numbers=3&numbers=2 → 2.0. "
                    + "Division by 0 follows double semantics (Infinity / NaN).")
    public Mono<CalculationResponse> divide(@RequestParam List<Double> numbers) {
        return calculate(CalculationOperation.DIVIDE, numbers);
    }

    @PostMapping("/calculate")
    @Operation(summary = "Calculate with a JSON request body",
            description = "Example body: {\"operation\": \"ADD\", \"numbers\": [1, 2, 3]}")
    public Mono<CalculationResponse> calculate(@Valid @RequestBody CalculationRequest request) {
        return calculate(request.operation(), request.numbers());
    }

    private Mono<CalculationResponse> calculate(CalculationOperation operation, List<Double> numbers) {
        // fromCallable runs the calculation lazily when the response is subscribed;
        // an IllegalArgumentException from the Calculator becomes an error signal (HTTP 400).
        return Mono.fromCallable(() -> new CalculationResponse(operation, numbers, apply(operation, numbers)));
    }

    private double apply(CalculationOperation operation, List<Double> numbers) {
        double[] values = numbers.stream().mapToDouble(Double::doubleValue).toArray();
        return switch (operation) {
            case ADD -> calculator.add(values);
            case MULTIPLY -> calculator.multiply(values);
            case DIVIDE -> calculator.divide(values);
            case SUBTRACT -> {
                if (values.length != 2) {
                    throw new IllegalArgumentException("Exactly two arguments must be provided for subtraction!");
                }
                yield calculator.subtract(values[0], values[1]);
            }
        };
    }
}
