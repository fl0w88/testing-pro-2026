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

package com.example.contract.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.example.api.DemoApiApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * PROVIDER-Vertragstest: Hält die echte Calculator-API den Vertrag ein, den der Consumer geschrieben hat?
 *
 * <p>Ablauf:
 * <ol>
 *   <li>{@code @SpringBootTest(webEnvironment = RANDOM_PORT)} startet die echte API auf einem freien Port.</li>
 *   <li>{@code @PactFolder} lädt alle Verträge für den Provider {@code calculator-api} aus {@code target/pacts}
 *       (dort hat sie der {@code CalculatorConsumerPactTest} abgelegt).</li>
 *   <li>Für <b>jede Interaktion</b> im Vertrag erzeugt JUnit einen eigenen Testfall ({@code @TestTemplate}):
 *       Pact schickt den aufgezeichneten Request an die API und vergleicht die echte Antwort mit der erwarteten.</li>
 * </ol>
 *
 * <p>Wichtig: Der Consumer-Test muss vorher gelaufen sein. {@code mvn test} erledigt das automatisch
 * (siehe Surefire-Konfiguration in der {@code pom.xml}).
 */
@SpringBootTest(classes = DemoApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("calculator-api")
@PactFolder("target/pacts")
class CalculatorProviderPactTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setTarget(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void verifyPact(PactVerificationContext context) {
        context.verifyInteraction();
    }

    /**
     * Provider State: der Consumer schreibt im Vertrag {@code given("the calculator is available")}.
     * Hier würde der Provider die nötigen Testdaten vorbereiten (z. B. in einer Datenbank).
     * Der Taschenrechner hat keinen Zustand, daher ist nichts zu tun.
     */
    @State("the calculator is available")
    void calculatorIsAvailable() {
        // nothing to prepare – the calculator is stateless
    }
}
