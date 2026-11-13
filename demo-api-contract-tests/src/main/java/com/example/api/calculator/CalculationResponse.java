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

import java.util.List;

/**
 * JSON response of all calculator endpoints, e.g.
 * {@code {"operation": "ADD", "numbers": [1.0, 2.0], "result": 3.0}}.
 */
public record CalculationResponse(CalculationOperation operation, List<Double> numbers, double result) {
}
