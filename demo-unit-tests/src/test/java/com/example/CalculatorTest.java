package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit-Tests für die Klasse {@link Calculator}.
 *
 * <p>Aufbau dieser Datei:
 * <ul>
 *   <li>Beispiele: zwei fertige Tests zum Nachlesen</li>
 *   <li>Übung 2: Grundrechenarten testen</li>
 *   <li>Übung 3: Ausnahmen und Grenzfälle testen</li>
 * </ul>
 *
 * <p>Branch loesungen-tag-1: Musterlösungen aller Übungen.
 */
class CalculatorTest {

    // Das Testobjekt: ein Calculator, der vor jedem Test neu erzeugt wird
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    // =====================================================================
    // Beispiele
    // =====================================================================

    @Test
    @DisplayName("Beispiel: 1 + 2 ergibt 3")
    void additionReturnsSumOfTwoNumbers() {
        // Arrange: der Calculator wurde bereits in setUp() erzeugt

        // Act: die zu testende Methode aufrufen
        double result = calculator.add(1, 2);

        // Assert: erwartetes Ergebnis zuerst, tatsächliches Ergebnis danach
        assertEquals(3.0, result);
    }

    @Test
    @DisplayName("Beispiel: 1 + 2 + 3 ergibt 6")
    void additionReturnsSumOfThreeNumbers() {
        double result = calculator.add(1, 2, 3);

        assertEquals(6.0, result);
    }

    // =====================================================================
    // Übung 2: Grundrechenarten testen
    // =====================================================================

    @Test
    @DisplayName("Übung 2a: 10 - 4 ergibt 6")
    void subtractionReturnsDifference() {
        // Act
        double result = calculator.subtract(10, 4);

        // Assert
        assertEquals(6.0, result);
    }

    @Test
    @DisplayName("Übung 2b: 3 * 4 ergibt 12")
    void multiplicationReturnsProduct() {
        double result = calculator.multiply(3, 4);

        assertEquals(12.0, result);
    }

    @Test
    @DisplayName("Übung 2c: 10 / 4 ergibt 2.5")
    void divisionReturnsQuotient() {
        double result = calculator.divide(10, 4);

        assertEquals(2.5, result);
    }

    // =====================================================================
    // Übung 3: Ausnahmen und Grenzfälle testen
    // =====================================================================

    @Test
    @DisplayName("Übung 3a: Addition mit nur einer Zahl löst eine IllegalArgumentException aus")
    void additionWithOnlyOneNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> calculator.add(5));
    }

    @Test
    @DisplayName("Übung 3b: BigDecimal-Division durch 0 löst eine IllegalArgumentException aus")
    void bigDecimalDivisionByZeroThrowsException() {
        // Arrange
        BigDecimal ten = new BigDecimal("10");
        BigDecimal zero = BigDecimal.ZERO;

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.divide(ten, zero));
    }

    @Test
    @DisplayName("Übung 3c: double-Division durch 0 – was passiert?")
    void doubleDivisionByZero() {
        // Act
        double result = calculator.divide(10, 0);

        // Assert: bei double gibt es keine Ausnahme, sondern den Wert "unendlich"
        assertEquals(Double.POSITIVE_INFINITY, result);
        // Diskussion: Die BigDecimal-Variante löst eine Ausnahme aus (Übung 3b), die
        // double-Variante nicht. Das Verhalten ist uneinheitlich; ob das gewollt ist,
        // muss die Anforderung klären, nicht der Test.
    }
}
