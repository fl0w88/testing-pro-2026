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

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * JSON request body for {@code POST /api/calculator/calculate}.
 */
@Schema(example = "{\"operation\": \"ADD\", \"numbers\": [1, 2, 3]}")
public record CalculationRequest(
        @NotNull @Schema(description = "Operation to perform", example = "ADD") CalculationOperation operation,
        @NotNull @Schema(description = "Operands", example = "[1, 2, 3]") List<Double> numbers) {
}
