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

Erwartetes Ergebnis beim ersten Start: **3 Tests bestanden, 8 Tests übersprungen**.
Die übersprungenen Tests sind die vorbereiteten Übungen (markiert mit `@Disabled`).

## Aufbau

```
demo-unit-tests
├── pom.xml                                  Maven-Konfiguration (JUnit 6, Log4j)
└── src
    ├── main/java/com/example                Produktcode (das Testobjekt)
    │   ├── Calculator.java                  Grundrechenarten für double und BigDecimal
    │   ├── WordSorter.java                  sortiert eine Liste von Wörtern
    │   └── StringComparator.java            legt fest, wie zwei Wörter verglichen werden
    └── test/java/com/example                Testcode
        ├── CalculatorTest.java              Beispiele, Übung 2 und Übung 3
        └── WordSorterTest.java              Beispiel und Übung 4
```

## Anforderungen an das Testobjekt

**Calculator**

- `add`, `multiply` und `divide` brauchen mindestens zwei Zahlen. Bei weniger Zahlen wird eine `IllegalArgumentException` ausgelöst.
- `divide` mit `BigDecimal`: Eine Division durch 0 löst eine `IllegalArgumentException` aus.

**WordSorter**

- Wörter werden alphabetisch aufsteigend sortiert.
- Groß- und Kleinschreibung wird dabei **nicht** unterschieden.

## Übungen

Vorgehen bei jeder Übung: `@Disabled` entfernen, die `TODO`-Kommentare umsetzen,
die Zeile mit `fail(...)` löschen und den Test ausführen.

### Übung 2: Grundrechenarten testen (12 Minuten) – `CalculatorTest`

| Teil | Testmethode | Erwartetes Ergebnis |
| --- | --- | --- |
| 2a | `subtractionReturnsDifference` | `calculator.subtract(10, 4)` ergibt `6.0` |
| 2b | `multiplicationReturnsProduct` | `calculator.multiply(3, 4)` ergibt `12.0` |
| 2c | `divisionReturnsQuotient` | `calculator.divide(10, 4)` ergibt `2.5` |

Erledigt, wenn: alle drei Tests grün sind und jeder Test dem Muster Arrange – Act – Assert folgt.

### Übung 3: Ausnahmen und Grenzfälle testen (10 Minuten) – `CalculatorTest`

| Teil | Testmethode | Erwartetes Ergebnis |
| --- | --- | --- |
| 3a | `additionWithOnlyOneNumberThrowsException` | `calculator.add(5)` löst eine `IllegalArgumentException` aus |
| 3b | `bigDecimalDivisionByZeroThrowsException` | `BigDecimal`-Division durch 0 löst eine `IllegalArgumentException` aus |
| 3c | `doubleDivisionByZero` | Erst vermuten, dann ausprobieren, dann das Verhalten festhalten |

Erledigt, wenn: alle drei Tests grün sind und Sie erklären können, warum sich 3b und 3c unterschiedlich verhalten.

### Übung 4: Einen Fehler im WordSorter finden (13 Minuten) – `WordSorterTest`

| Teil | Aufgabe |
| --- | --- |
| 4a | Test `sortingIgnoresUpperAndLowerCase` schreiben: `"bert", "Anna", "Zoe"` muss `"Anna", "bert", "Zoe"` ergeben. Der Test schlägt fehl – das ist gewollt. |
| 4b | Den Fehlerzustand in `StringComparator.java` finden und beheben. Danach **alle** Tests erneut ausführen. |
| 4c | Zusatz: Test `emptyListStaysEmpty` schreiben. |

Erledigt, wenn: alle Tests grün sind und Sie benennen können, was hier Fehlerwirkung und was Fehlerzustand war.

Zusatzfragen für Schnelle:

- Wie sortiert der WordSorter `"Äpfel", "Apfel", "Zebra"`? Entspricht das der deutschen Sortierung im Wörterbuch?
- Was passiert bei `WordSorter.sortWords(List.of("b", "a"))`? Warum?

## Lösungen (dieser Branch)

Dieser Branch enthält die Musterlösungen. Alle 11 Tests sind grün.
