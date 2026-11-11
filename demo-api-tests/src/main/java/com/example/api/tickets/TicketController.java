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

package com.example.api.tickets;

import com.example.tickets.ViennaLinesTicketMachine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

/**
 * REST endpoints for the {@link ViennaLinesTicketMachine} (single trip tickets).
 * <p>
 * The holiday lookup (Jollyday) may read calendar files, so the work is moved to the
 * {@code boundedElastic} scheduler. This keeps the Netty event loop free for other requests.
 */
@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Tickets", description = "Vienna Lines single trip tickets (prices 2026)")
public class TicketController {

    private final ViennaLinesTicketMachine ticketMachine;

    public TicketController(ViennaLinesTicketMachine ticketMachine) {
        this.ticketMachine = ticketMachine;
    }

    @GetMapping("/price")
    @Operation(summary = "Get the single trip price",
            description = "Under 6: free. 6–14: 1.60 €, free on Sundays, public holidays and Vienna school holidays. "
                    + "15+: 3.20 €. Without dateTime the current date/time is used.")
    public Mono<TicketPriceResponse> getPrice(
            @Parameter(description = "Age of the passenger", example = "10")
            @RequestParam int age,
            @Parameter(description = "Date and time of the trip (ISO-8601)", example = "2026-11-11T10:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return Mono.fromCallable(() -> {
                    LocalDateTime tripTime = dateTime != null ? dateTime : LocalDateTime.now();
                    return new TicketPriceResponse(age, tripTime,
                            ticketMachine.getSingleTripTicketPrice(age, tripTime), "EUR");
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Buy a single trip ticket",
            description = "Returns the ticket including change. Paying less than the price returns HTTP 400.")
    public Mono<TicketResponse> buyTicket(@Valid @RequestBody TicketRequest request) {
        return Mono.fromCallable(() -> {
                    LocalDateTime tripTime = request.dateTime() != null ? request.dateTime() : LocalDateTime.now();
                    return TicketResponse.from(
                            ticketMachine.getSingleTripTicket(request.age(), request.payment(), tripTime));
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
