# demo-unit-tests

Kursprojekt für Tag 1 des Kurses „Einführung in die Testautomatisierung“:
erste Unit-Tests mit **JUnit 6**.

## Voraussetzungen

| Werkzeug | Version |
| --- | --- |
| JDK | 17 oder neuer (JUnit 6 setzt Java 17 voraus) |
| Maven | 3.9 oder neuer (in IntelliJ bereits enthalten) |
| JUnit | 6.0.3 (wird von Maven automatisch geladen) |

## Projekt starten

1. Repository klonen: `git clone https://github.com/fl0w88/testing-pro-2026.git`
2. In IntelliJ den Ordner `demo-unit-tests` öffnen (File → Open → `pom.xml` → „Open as Project“).
3. Tests ausführen:
   - in IntelliJ: Rechtsklick auf `src/test/java` → „Run 'All Tests'“
   - oder im Terminal: `mvn clean test`

Erwartetes Ergebnis beim ersten Start: **3 Tests bestanden, 8 Tests fehlgeschlagen**.
Das ist gewollt: Die fehlschlagenden Tests sind die vorbereiteten Übungen. Sie enthalten
nur das Gerüst (Name, `@DisplayName`, `TODO`-Kommentare) und enden mit `fail(...)`.
Ein Test wird erst grün, wenn Sie ihn ausgefüllt haben.

## Aufbau

```
demo-unit-tests
├── pom.xml                                  Maven-Konfiguration (JUnit 6, SLF4J + Logback)
└── src
    ├── main/resources/logback.xml           Logging-Konfiguration (Konsole und logs/)
    ├── main/java/com/example                Produktcode (das Testobjekt)
    │   ├── Calculator.java                  Grundrechenarten für double und BigDecimal
    │   ├── WordSorter.java                  sortiert eine Liste von Wörtern
    │   └── StringComparator.java            legt fest, wie zwei Wörter verglichen werden
    └── test/java/com/example                Testcode
        ├── CalculatorTest.java              Beispiel, Aufwärmübung, Übung 2 und Übung 3
        └── WordSorterTest.java              Beispiel und Übung 4
```

## Anforderungen an das Testobjekt

**Calculator**

- `add`, `multiply` und `divide` brauchen mindestens zwei Zahlen. Bei weniger Zahlen wird eine `IllegalArgumentException` ausgelöst.
- alle Operationen sollen abgetestet werden

Die Methoden im Überblick (jeweils für `double` und `BigDecimal` vorhanden):

| Methode | Parameter (`double`-Variante) | Rückgabe |
| --- | --- | --- |
| `add` | beliebig viele Zahlen, mindestens zwei | Summe |
| `subtract` | genau zwei Zahlen | Differenz |
| `multiply` | beliebig viele Zahlen, mindestens zwei | Produkt |
| `divide` | beliebig viele Zahlen, mindestens zwei | Quotient (von links nach rechts) |

**WordSorter**

- Wörter werden alphabetisch aufsteigend sortiert.
- Groß- und Kleinschreibung wird dabei **nicht** unterschieden.

## Übungen

Vorgehen bei jeder Übung:

1. Den Test einmal ausführen und sehen, dass er rot ist (`fail(...)`).
2. Die `TODO`-Kommentare umsetzen.
3. Die Zeile mit `fail(...)` löschen.
4. Den Test erneut ausführen – er soll jetzt grün sein.

### Werkzeugkasten: Ein Unit-Test in JUnit 6

Jeder Test folgt dem Muster **Arrange – Act – Assert**:

| Schritt | Frage | Im Code |
| --- | --- | --- |
| Arrange | Was brauche ich für den Test? | Testobjekt und Eingabewerte anlegen (der `Calculator` entsteht schon in `setUp()`) |
| Act | Was teste ich? | **genau eine** Methode des Testobjekts aufrufen, Ergebnis in einer Variablen speichern |
| Assert | Was erwarte ich? | erwartetes mit tatsächlichem Ergebnis vergleichen |

Vorlage ist der fertige Test `additionReturnsSumOfTwoNumbers` in `CalculatorTest`.

Die wichtigsten Prüfmethoden aus `org.junit.jupiter.api.Assertions` (`assertEquals` und `fail` sind in `CalculatorTest` bereits importiert, `assertThrows` und `java.math.BigDecimal` müssen Sie für Übung 3 selbst importieren):

| Methode | Wofür | Allgemeine Form |
| --- | --- | --- |
| `assertEquals` | Zwei Werte sind gleich | `assertEquals(erwartet, tatsächlich)` |
| `assertThrows` | Ein Aufruf löst eine bestimmte Ausnahme aus | `assertThrows(AusnahmeKlasse.class, () -> aufruf())` |
| `fail` | Test absichtlich scheitern lassen | `fail("Begründung")` |

Hinweise:

- Bei `assertEquals` steht der **erwartete** Wert immer zuerst. Sonst ist die Fehlermeldung irreführend.
- Der Calculator rechnet mit `double`. Schreiben Sie erwartete Werte deshalb als `double`, z. B. `6.0` statt `6`.
- Bei `assertThrows` wird der Aufruf in ein Lambda `() -> ...` verpackt. So kann JUnit den Aufruf selbst ausführen und die Ausnahme abfangen.
- `BigDecimal`-Werte legt man am besten aus einem String an: `new BigDecimal("1.5")`.

### Aufwärmübung – `CalculatorTest`

| Testmethode | Erwartetes Ergebnis |
| --- | --- |
| `additionReturnsSumOfThreeNumbers` | `calculator.add(1, 2, 3)` = `6.0` |

Erledigt, wenn: der Test grün ist. Bonus: Lassen Sie den Test einmal absichtlich scheitern
(z. B. falscher erwarteter Wert) und lesen Sie die Fehlermeldung.

### Übung 2: Grundrechenarten testen 
Erledigt, wenn: alle drei Tests grün sind und jeder Test dem Muster Arrange – Act – Assert folgt.

Zusatzfragen für Schnelle:

- Welche Werte wären für `subtract` noch interessant (negative Zahlen, 0, Kommazahlen)?
- `calculator.add(0.1, 0.2)` = ? Wie würden Sie das testen?

### Übung 3: Ausnahmen und Grenzfälle testen – `CalculatorTest`

| Teil | Testmethode | Erwartetes Ergebnis |
| --- | --- | --- |
| 3a | `additionWithOnlyOneNumberThrowsException` | `calculator.add(5)` löst eine `IllegalArgumentException` aus |
| 3b | `bigDecimalDivisionByZeroThrowsException` | `BigDecimal`-Division durch 0 löst eine `IllegalArgumentException` aus |
| 3c | `doubleDivisionByZero` | Erst vermuten (als Kommentar notieren), dann ausprobieren, dann das beobachtete Verhalten mit einem `assert...` festhalten |

Erledigt, wenn: alle drei Tests grün sind und Sie erklären können, warum sich 3b und 3c unterschiedlich verhalten.

### Übung 4: Einen Fehler im WordSorter finden – `WordSorterTest`

| Teil | Aufgabe |
| --- | --- |
| 4a | Test `sortingIgnoresUpperAndLowerCase` schreiben: sortiert `"bert", "Anna", "Zoe"` = `"Anna", "bert", "Zoe"`. Der Test schlägt fehl – das ist gewollt. |
| 4b | Den Fehlerzustand in `StringComparator.java` finden und beheben. Danach **alle** Tests erneut ausführen. |
| 4c | Zusatz: Test `emptyListStaysEmpty` schreiben. |

Erledigt, wenn: Alle Tests grün sind und Sie benennen können, was hier Fehlerwirkung und was Fehlerzustand war.

Zusatzfragen für Schnelle:
- Wie prüft man den Author/E-Mail des Taschenrechners?
- Wie sortiert der WordSorter `"Äpfel", "Apfel", "Zebra"`? Entspricht das der deutschen Sortierung im Wörterbuch?
- Was passiert bei `WordSorter.sortWords(List.of("b", "a"))`? Warum?

