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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * API-Tests für die Addition: {@code GET /api/calculator/add} und {@code POST /api/calculator/calculate}.
 *
 * <p>{@code @SpringBootTest(webEnvironment = RANDOM_PORT)} startet die komplette Anwendung auf einem
 * freien Port – genau so, als würden Sie {@code mvn spring-boot:run} ausführen. Der {@link WebTestClient}
 * schickt echte HTTP-Requests an diese Anwendung und prüft Statuscode, Header und JSON-Body.
 *
 * <p>Jeder Test folgt dem Muster Arrange – Act – Assert:
 * <ul>
 *   <li>Arrange: Request zusammenbauen (URL, Parameter, Body)</li>
 *   <li>Act: Request abschicken ({@code exchange()})</li>
 *   <li>Assert: Statuscode und Antwort prüfen ({@code expectStatus()}, {@code expectBody()})</li>
 * </ul>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CalculatorAdditionApiTest {

    @Autowired
    private WebTestClient webTestClient;

    // =====================================================================
    // GET mit Query-Parametern
    // =====================================================================

    @Test
    @DisplayName("GET: 1 + 2 = 3")
    void addTwoNumbers() {
        webTestClient.get()
                .uri("/api/calculator/add?numbers=1&numbers=2")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.operation").isEqualTo("ADD")
                .jsonPath("$.numbers.length()").isEqualTo(2)
                .jsonPath("$.result").isEqualTo(3.0);
    }

    // =====================================================================
    // POST mit JSON-Body
    // =====================================================================

    @Test
    @DisplayName("POST: 1 + 2 + 3 = 6 mit JSON-Body")
    void addWithJsonBody() {
        webTestClient.post()
                .uri("/api/calculator/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "operation": "ADD",
                          "numbers": [1, 2, 3]
                        }
                        """)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.operation").isEqualTo("ADD")
                .jsonPath("$.result").isEqualTo(6.0);
    }


}
