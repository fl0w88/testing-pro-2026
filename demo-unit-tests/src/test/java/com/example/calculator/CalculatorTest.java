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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @DisplayName("Beispiel Addition BigDecimal: 1 + 2 = 3")
    void additionBigDecimal() {
         BigDecimal zahl1 = new BigDecimal("1");
         BigDecimal zahl2 = new BigDecimal("2");

         BigDecimal result = calculator.add(zahl1, zahl2);
         assertEquals(new BigDecimal("3"), result);
    }

    @Test
    @DisplayName("Example subtraction: 7 - 2 = 5")
    void subtractionBigDecimal() {
        BigDecimal zahl1 = new BigDecimal("7");
        BigDecimal zahl2 = new BigDecimal("5");

        BigDecimal subtract = calculator.subtract(zahl1, zahl2);
        assertEquals(new BigDecimal("2"), subtract);
    }

    @Test
    @DisplayName("Example multiplication: 7 * 2 = 14")
    void multiplicationBigDecimal() {
        BigDecimal zahl1 = new BigDecimal("7");
        BigDecimal zahl2 = new BigDecimal("2");

        BigDecimal result = calculator.multiply(zahl1, zahl2);
        assertEquals(new BigDecimal("14"), result);
        assertTrue(new BigDecimal("14").compareTo(result) == 0);
    }

    @Test
    @DisplayName("Example division: 7 / 2 = 3.5")
    void divisionBigDecimal() {
        BigDecimal nr1 = new BigDecimal("7");
        BigDecimal nr2 = new BigDecimal("2");

        BigDecimal result = calculator.divide(nr1, nr2);
        assertTrue(new BigDecimal("3.5").compareTo(result) == 0);
    }

    // =====================================================================
    // Aufwärmübung
    // =====================================================================

    @Test
    @DisplayName("Aufwärmen: 1 + 2 + 3 = 6")
    void additionReturnsSumOfThreeNumbers() {
        // Arrange: der Calculator wurde bereits in setUp() erzeugt

        // Act: die zu testende Methode aufrufen
        double result = calculator.add(1, 2, 3);

        // Assert: erwartetes Ergebnis zuerst, tatsächliches Ergebnis danach
        assertEquals(6.0, result);
    }

    @Test
    @DisplayName("Example division by 0: 7 / 0 = ?")
    void divisionBy0BigDecimal() {
        BigDecimal nr1 = new BigDecimal("7");
        BigDecimal nr2 = new BigDecimal("0");

        try {
            calculator.divide(nr1, nr2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot divide something by 0.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Calculator has an author")
    void calculatorHasAuthor() {
        Assertions.assertNotNull(calculator.authorName);
        Assertions.assertNotNull(calculator.authorEmail);
    }




    // =====================================================================
    // Übung 2: Grundrechenarten testen
    // =====================================================================

    @Test
    @DisplayName("Übung 2a: 10 - 4 = 6")
    void subtractionReturnsDifference() {
        double result = calculator.subtract(10, 4);
        assertEquals((10-4), result);
    }

    @Test
    @DisplayName("Übung 2b: 3 * 4 = 12")
    void multiplicationReturnsProduct() {
        double result = calculator.multiply(3, 4);
        assertEquals(12, result);
    }

    @Test
    @DisplayName("Übung 2c: 10 / 4 = 2.5")
    void divisionReturnsQuotient() {
        double result = calculator.divide(10, 4);
        assertEquals(2.5, result);
    }

    @Test
    @DisplayName("Übung 2c: 10 / 4 = 2.5")
    void divisionReturnsQuotient2() {
        double result = calculator.divide(7, 3.5);
        assertEquals(2.0, result);
    }

    // =====================================================================
    // Übung 3: Ausnahmen und Grenzfälle testen
    // =====================================================================

    @Test
    @DisplayName("Übung 3a: Addition mit nur einer Zahl löst eine IllegalArgumentException aus")
    void additionWithOnlyOneNumberThrowsException() {
        try {
            calculator.add(1);
            fail("I should see an error");
        } catch (IllegalArgumentException e) {
            assertEquals("At least two arguments must be provided!", e.getMessage());
        }

    }

}
