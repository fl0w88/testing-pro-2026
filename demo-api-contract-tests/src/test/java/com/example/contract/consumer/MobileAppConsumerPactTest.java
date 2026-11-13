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
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.example.client.CalculationResult;
import com.example.client.CalculatorClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Übung C3 (siehe README): Ein ZWEITER Consumer – das Team der Taschenrechner-App fürs Handy.
 *
 * <p>Das App-Team hat seinen Vertrag selbst geschrieben, ohne mit dem Provider-Team zu sprechen.
 * Dieser Test ist grün. Der Provider-Test ({@code CalculatorProviderPactTest}) prüft danach
 * automatisch auch diesen Vertrag – und wird rot.
 *
 * <p>Erst bearbeiten, wenn Übung C2 erledigt ist (alle Tests grün): dann {@code @Disabled} entfernen.
 *
 * <p>Aufgabe: Finden Sie heraus, warum. Ändern Sie dazu zuerst NICHTS, sondern lesen Sie die Ausgabe
 * des Provider-Tests und die Pact-Datei {@code target/pacts/calculator-mobile-app-calculator-api.json}.
 */
@Disabled("Übung C3: entfernen, sobald Übung C2 erledigt ist")
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "calculator-api", pactVersion = PactSpecVersion.V4)
class MobileAppConsumerPactTest {

    @Pact(consumer = "calculator-mobile-app")
    V4Pact multiplyThreeNumbers(PactDslWithProvider builder) {
        return builder
                .given("the calculator is available")
                .uponReceiving("a GET request to multiply 2, 3 and 4")
                    .method("GET")
                    .path("/api/calculator/multiply")
                    .query("numbers=2.0&numbers=3.0&numbers=4.0")
                .willRespondWith()
                    .status(200)
                    .body(new PactDslJsonBody()
                            .stringValue("operation", "MULTIPLY")
                            // Die App zeigt das Ergebnis ohne Nachkommastellen an
                            .integerType("result", 24))
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "multiplyThreeNumbers")
    @DisplayName("Mobile App: 2 * 3 * 4 = 24")
    void multiplyThreeNumbers(MockServer mockServer) {
        CalculatorClient client = new CalculatorClient(mockServer.getUrl());

        CalculationResult result = client.multiply(2, 3, 4);

        assertThat(result.operation()).isEqualTo("MULTIPLY");
        assertThat(result.result()).isEqualTo(24.0);
    }
}
