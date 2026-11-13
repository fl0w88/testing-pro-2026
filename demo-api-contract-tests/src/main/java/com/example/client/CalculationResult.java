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

import java.util.List;

/**
 * The consumer's own view of a calculator response.
 * <p>
 * Deliberately NOT the provider class {@code com.example.api.calculator.CalculationResponse}:
 * consumer and provider are usually separate code bases, and the only thing they share is the
 * contract (the pact file).
 */
public record CalculationResult(String operation, List<Double> numbers, double result) {
}
