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

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JSON request body for {@code POST /api/tickets}.
 * Negative ages and insufficient payments are rejected by the ticket machine itself (HTTP 400).
 */
@Schema(example = "{\"age\": 30, \"payment\": 5.00, \"dateTime\": \"2026-11-11T10:00:00\"}")
public record TicketRequest(
        @NotNull @Schema(description = "Age of the passenger", example = "30") Integer age,
        @NotNull @Schema(description = "Amount paid in EUR", example = "5.00") BigDecimal payment,
        @Schema(description = "Date/time of the trip (ISO-8601), optional – default: now",
                example = "2026-11-11T10:00:00") LocalDateTime dateTime) {
}
