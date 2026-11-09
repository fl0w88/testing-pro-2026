package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit-Tests für die Klasse {@link WordSorter}.
 *
 * <p>Anforderung an den WordSorter (siehe README.md):
 * Wörter werden alphabetisch aufsteigend sortiert.
 * Groß- und Kleinschreibung wird dabei nicht unterschieden.
 *
 * <p>Übung 4: einen Fehlerzustand im WordSorter finden und beheben.
 */
class WordSorterTest {

    // =====================================================================
    // Beispiel
    // =====================================================================

    @Test
    @DisplayName("Beispiel: drei großgeschriebene Namen werden alphabetisch sortiert")
    void sortsCapitalizedNamesAlphabetically() {
        // Arrange: eine veränderbare Liste (ArrayList), weil sortWords die Liste selbst sortiert
        List<String> names = new ArrayList<>(List.of("Maria", "Ana", "Laura"));

        // Act
        List<String> result = WordSorter.sortWords(names);

        // Assert: Listen werden mit equals verglichen – Reihenfolge und Inhalt müssen stimmen
        assertEquals(List.of("Ana", "Laura", "Maria"), result);
    }

    // =====================================================================
    // Übung 4: Fehler im WordSorter finden
    // =====================================================================

    @Test
    @DisplayName("Übung 4a: Groß- und Kleinschreibung wird beim Sortieren nicht unterschieden")
    void sortingIgnoresUpperAndLowerCase() {
        // Arrange
        List<String> words = new ArrayList<>(List.of("bert", "Anna", "Zoe"));

        // Act
        List<String> result = WordSorter.sortWords(words);

        // Assert
        assertEquals(List.of("Anna", "bert", "Zoe"), result);
    }

    @Test
    @DisplayName("Übung 4c (Zusatz): eine leere Liste bleibt leer")
    void emptyListStaysEmpty() {
        List<String> result = WordSorter.sortWords(new ArrayList<>());

        assertTrue(result.isEmpty());
    }
}
