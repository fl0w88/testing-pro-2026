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

package com.example.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit-Tests für die Klasse {@link Calculator}.
 *
 * <p>Aufbau dieser Datei:
 * <ul>
 *   <li>Beispiel: ein fertiger Test zum Nachlesen</li>
 *   <li>Aufwärmübung: einen zweiten Additionstest selbst schreiben</li>
 *   <li>Übung 2: Grundrechenarten testen</li>
 *   <li>Übung 3: Ausnahmen und Grenzfälle testen</li>
 * </ul>
 *
 * <p>Alle Übungstests schlagen zunächst mit {@code fail(...)} fehl. Vorgehen siehe README.md:
 * TODOs umsetzen, die Zeile mit {@code fail(...)} löschen, Test ausführen.
 */
class CalculatorTest {

    // Das Testobjekt: ein Calculator, der vor jedem Test neu erzeugt wird
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    // =====================================================================
    // Beispiel
    // =====================================================================

    @Test
    @DisplayName("Beispiel: 1 + 2 = 3")
    void additionReturnsSumOfTwoNumbers() {
        // Arrange: der Calculator wurde bereits in setUp() erzeugt

        // Act: die zu testende Methode aufrufen
        double result = calculator.add(1, 2);

        // Assert: erwartetes Ergebnis zuerst, tatsächliches Ergebnis danach
        assertEquals(3.0, result);
    }

    // =====================================================================
    // Aufwärmübung
    // =====================================================================

    @Test
    @DisplayName("Aufwärmen: 1 + 2 + 3 = 6")
    void additionReturnsSumOfThreeNumbers() {
        // TODO Act: add mit drei Zahlen aufrufen und das Ergebnis speichern

        // TODO Assert: erwartetes und tatsächliches Ergebnis vergleichen

        fail("Noch nicht implementiert");
    }

    // =====================================================================
    // Übung 2: Grundrechenarten testen
    // =====================================================================

    @Test
    @DisplayName("Übung 2a: 10 - 4 = 6")
    void subtractionReturnsDifference() {
        // TODO Act

        // TODO Assert

        fail("Noch nicht implementiert");
    }

    @Test
    @DisplayName("Übung 2b: 3 * 4 = 12")
    void multiplicationReturnsProduct() {
        // TODO Act

        // TODO Assert

        fail("Noch nicht implementiert");
    }

    @Test
    @DisplayName("Übung 2c: 10 / 4 = 2.5")
    void divisionReturnsQuotient() {
        // TODO Act

        // TODO Assert

        fail("Noch nicht implementiert");
    }

    // =====================================================================
    // Übung 3: Ausnahmen und Grenzfälle testen
    // =====================================================================

    @Test
    @DisplayName("Übung 3a: Addition mit nur einer Zahl löst eine IllegalArgumentException aus")
    void additionWithOnlyOneNumberThrowsException() {
        // TODO Act + Assert: prüfen, dass der Aufruf die erwartete Ausnahme auslöst

        fail("Noch nicht implementiert");
    }

}
