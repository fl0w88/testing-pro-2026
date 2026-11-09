package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
 * <p>Vorgehen bei jeder Übung: {@code @Disabled} entfernen, die TODO-Kommentare umsetzen,
 * die Zeile mit {@code fail(...)} löschen und den Test ausführen.
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
    @Disabled("Übung 2a: @Disabled entfernen und den Test schreiben")
    @DisplayName("Übung 2a: 10 - 4 ergibt 6")
    void subtractionReturnsDifference() {
        // TODO Act: calculator.subtract(10, 4) aufrufen und das Ergebnis speichern
        // TODO Assert: das Ergebnis mit 6.0 vergleichen
        fail("Übung 2a ist noch nicht umgesetzt");
    }

    @Test
    @Disabled("Übung 2b: @Disabled entfernen und den Test schreiben")
    @DisplayName("Übung 2b: 3 * 4 ergibt 12")
    void multiplicationReturnsProduct() {
        // TODO Act: calculator.multiply(3, 4) aufrufen
        // TODO Assert: das Ergebnis mit 12.0 vergleichen
        fail("Übung 2b ist noch nicht umgesetzt");
    }

    @Test
    @Disabled("Übung 2c: @Disabled entfernen und den Test schreiben")
    @DisplayName("Übung 2c: 10 / 4 ergibt 2.5")
    void divisionReturnsQuotient() {
        // TODO Act: calculator.divide(10, 4) aufrufen
        // TODO Assert: das Ergebnis mit 2.5 vergleichen
        fail("Übung 2c ist noch nicht umgesetzt");
    }

    // =====================================================================
    // Übung 3: Ausnahmen und Grenzfälle testen
    // =====================================================================

    @Test
    @Disabled("Übung 3a: @Disabled entfernen und den Test schreiben")
    @DisplayName("Übung 3a: Addition mit nur einer Zahl löst eine IllegalArgumentException aus")
    void additionWithOnlyOneNumberThrowsException() {
        // TODO assertThrows verwenden:
        //      assertThrows(IllegalArgumentException.class, () -> calculator.add(5));
        //      (import static org.junit.jupiter.api.Assertions.assertThrows; ergänzen)
        fail("Übung 3a ist noch nicht umgesetzt");
    }

    @Test
    @Disabled("Übung 3b: @Disabled entfernen und den Test schreiben")
    @DisplayName("Übung 3b: BigDecimal-Division durch 0 löst eine IllegalArgumentException aus")
    void bigDecimalDivisionByZeroThrowsException() {
        // TODO Arrange: zwei Werte anlegen: new BigDecimal("10") und BigDecimal.ZERO
        //      (import java.math.BigDecimal; ergänzen)
        // TODO Act + Assert: mit assertThrows prüfen, dass calculator.divide(...) eine
        //      IllegalArgumentException auslöst
        fail("Übung 3b ist noch nicht umgesetzt");
    }

    @Test
    @Disabled("Übung 3c: @Disabled entfernen und den Test schreiben")
    @DisplayName("Übung 3c: double-Division durch 0 – was passiert?")
    void doubleDivisionByZero() {
        // TODO 1. Vermuten Sie zuerst: Was liefert calculator.divide(10, 0)?
        // TODO 2. Probieren Sie es aus (z.B. mit System.out.println).
        // TODO 3. Halten Sie das tatsächliche Verhalten in einer Assertion fest.
        //         Tipp: Double.POSITIVE_INFINITY
        // Diskussion: Ist dieses Verhalten gewollt? Vergleichen Sie mit Übung 3b.
        fail("Übung 3c ist noch nicht umgesetzt");
    }
}
