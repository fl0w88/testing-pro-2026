package com.example;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
    @Disabled("Übung 4a: @Disabled entfernen und den Test schreiben")
    @DisplayName("Übung 4a: Groß- und Kleinschreibung wird beim Sortieren nicht unterschieden")
    void sortingIgnoresUpperAndLowerCase() {
        // TODO Arrange: new ArrayList<>(List.of("bert", "Anna", "Zoe"))
        // TODO Act: WordSorter.sortWords(...) aufrufen
        // TODO Assert: laut Anforderung wird List.of("Anna", "bert", "Zoe") erwartet
        // Führen Sie den Test aus. Schlägt er fehl? Lesen Sie die Meldung genau:
        // Was war erwartet, was wurde geliefert?
        fail("Übung 4a ist noch nicht umgesetzt");
    }

    // Übung 4b: Den Fehlerzustand im Produktcode finden (Tipp: StringComparator.java),
    //           beheben und ALLE Tests erneut ausführen. Hier ist kein neuer Test nötig.

    @Test
    @Disabled("Übung 4c (Zusatz): @Disabled entfernen und den Test schreiben")
    @DisplayName("Übung 4c (Zusatz): eine leere Liste bleibt leer")
    void emptyListStaysEmpty() {
        // TODO Arrange: new ArrayList<String>()
        // TODO Act: WordSorter.sortWords(...) aufrufen
        // TODO Assert: assertTrue(result.isEmpty()) oder assertEquals(List.of(), result)
        fail("Übung 4c ist noch nicht umgesetzt");
    }
}
