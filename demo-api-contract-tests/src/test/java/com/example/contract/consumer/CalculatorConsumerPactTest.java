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

package com.example.contract.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslJsonRootValue;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.example.client.CalculationResult;
import com.example.client.CalculatorClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * CONSUMER-Vertragstest: Was erwartet der {@link CalculatorClient} von der Calculator-API?
 *
 * <p>Ablauf (consumer-driven contract testing mit <a href="https://pact.io">Pact</a>):
 * <ol>
 *   <li>Jede {@code @Pact}-Methode beschreibt eine Interaktion: "Wenn ich DIESEN Request schicke,
 *       erwarte ich DIESE Antwort."</li>
 *   <li>Pact startet einen <b>Mock-Server</b>, der genau so antwortet. Die echte API läuft NICHT.</li>
 *   <li>Der Test ruft den echten {@link CalculatorClient} gegen den Mock-Server auf und prüft das Ergebnis.</li>
 *   <li>Sind alle Tests grün, schreibt Pact den Vertrag als JSON-Datei:
 *       {@code target/pacts/calculator-client-calculator-api.json}.</li>
 * </ol>
 * Diese Datei wird anschließend vom Provider-Test ({@code CalculatorProviderPactTest}) gegen die echte API geprüft.
 *
 * <p>Matcher wie {@code numberType} oder {@code stringMatcher} prüfen nur Typ bzw. Format – nicht den exakten Wert.
 * So ist der Vertrag nur so streng, wie der Consumer es wirklich braucht.
 */
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "calculator-api", pactVersion = PactSpecVersion.V4)
class CalculatorConsumerPactTest {

    private static final String CONSUMER = "calculator-client";
    private static final String CALCULATOR_AVAILABLE = "the calculator is available";
    private static final String JSON = "application/json(;.*)?";
    private static final String PROBLEM_JSON = "application/problem\\+json(;.*)?";

    // =====================================================================
    // Interaktion 1: GET /api/calculator/add?numbers=1.0&numbers=2.0
    // =====================================================================

    @Pact(consumer = CONSUMER)
    V4Pact addTwoNumbers(PactDslWithProvider builder) {
        return builder
                .given(CALCULATOR_AVAILABLE)
                .uponReceiving("a GET request to add 1 and 2")
                    .method("GET")
                    .path("/api/calculator/add")
                    .query("numbers=1.0&numbers=2.0")
                .willRespondWith()
                    .status(200)
                    .matchHeader("Content-Type", JSON, "application/json")
                    .body(new PactDslJsonBody()
                            .stringValue("operation", "ADD")
                            .minArrayLike("numbers", 2, PactDslJsonRootValue.numberType(1.0), 2)
                            .numberValue("result", 3.0))
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "addTwoNumbers")
    @DisplayName("GET: 1 + 2 = 3")
    void addTwoNumbers(MockServer mockServer) {
        CalculatorClient client = new CalculatorClient(mockServer.getUrl());

        CalculationResult result = client.add(1, 2);

        assertThat(result.operation()).isEqualTo("ADD");
        assertThat(result.result()).isEqualTo(3.0);
    }

    // =====================================================================
    // Interaktion 2: POST /api/calculator/calculate mit JSON-Body
    // =====================================================================

    @Pact(consumer = CONSUMER)
    V4Pact calculateWithJsonBody(PactDslWithProvider builder) {
        return builder
                .given(CALCULATOR_AVAILABLE)
                .uponReceiving("a POST request to add three numbers")
                    .method("POST")
                    .path("/api/calculator/calculate")
                    .matchHeader("Content-Type", JSON, "application/json")
                    .body(new PactDslJsonBody()
                            .stringValue("operation", "ADD")
                            // beliebig viele Zahlen (mind. 2); im Vertrag als Beispiel [2.0, 2.0, 2.0]
                            .minArrayLike("numbers", 2, PactDslJsonRootValue.numberType(2.0), 3))
                .willRespondWith()
                    .status(200)
                    .matchHeader("Content-Type", JSON, "application/json")
                    .body(new PactDslJsonBody()
                            .stringMatcher("operation", "ADD|SUBTRACT|MULTIPLY|DIVIDE", "ADD")
                            .minArrayLike("numbers", 2, PactDslJsonRootValue.numberType(2.0), 3)
                            // nur der Typ zählt: der Consumer braucht "eine Zahl", nicht genau 6.0
                            .numberType("result", 6.0))
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "calculateWithJsonBody")
    @DisplayName("POST: 1 + 2 + 3 mit JSON-Body")
    void calculateWithJsonBody(MockServer mockServer) {
        CalculatorClient client = new CalculatorClient(mockServer.getUrl());

        CalculationResult result = client.calculate("ADD", 1, 2, 3);

        assertThat(result.operation()).isEqualTo("ADD");
        assertThat(result.numbers()).hasSize(3);
    }

    // =====================================================================
    // Interaktion 3: Fehlerfall – nur eine Zahl → HTTP 400 (Problem Detail, RFC 9457)
    // =====================================================================

    @Pact(consumer = CONSUMER)
    V4Pact addWithOnlyOneNumber(PactDslWithProvider builder) {
        return builder
                .given(CALCULATOR_AVAILABLE)
                .uponReceiving("a GET request to add only one number")
                    .method("GET")
                    .path("/api/calculator/add")
                    .query("numbers=1.0")
                .willRespondWith()
                    .status(400)
                    .matchHeader("Content-Type", PROBLEM_JSON, "application/problem+json")
                    .body(new PactDslJsonBody()
                            .integerType("status", 400)
                            .stringType("detail", "At least two arguments must be provided!"))
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "addWithOnlyOneNumber")
    @DisplayName("GET: nur eine Zahl → 400 Bad Request")
    void addWithOnlyOneNumber(MockServer mockServer) {
        CalculatorClient client = new CalculatorClient(mockServer.getUrl());

        assertThatThrownBy(() -> client.add(1))
                .isInstanceOf(WebClientResponseException.BadRequest.class);
    }
}
