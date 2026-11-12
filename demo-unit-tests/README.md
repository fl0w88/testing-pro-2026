# demo-unit-tests

Kursprojekt für Tag 1 des Kurses „Einführung in die Testautomatisierung“:
erste Unit-Tests mit **JUnit 6**.

## Voraussetzungen

| Werkzeug | Version |
| --- | --- |
| JDK | 17 oder neuer (JUnit 6 setzt Java 17 voraus) |
| Maven | 3.9 oder neuer (in IntelliJ bereits enthalten) |
| JUnit | 6.1.3 (wird von Maven automatisch geladen) |
| Jollyday | 2.24.0 – Feiertagskalender (wird von Maven automatisch geladen) |

## Projekt starten

1. Repository klonen: `git clone https://github.com/fl0w88/testing-pro-2026.git`
2. In IntelliJ den Ordner `demo-unit-tests` öffnen (File → Open → `pom.xml` → „Open as Project“).
3. Tests ausführen:
   - in IntelliJ: Rechtsklick auf `src/test/java` → „Run 'All Tests'“
   - oder im Terminal: `mvn clean test`

Erwartetes Ergebnis beim ersten Start: **3 Tests bestanden, 6 Tests fehlgeschlagen**.
Das ist gewollt: Die fehlschlagenden Tests sind die vorbereiteten Übungen. Sie enthalten
nur das Gerüst (Name, `@DisplayName`, `TODO`-Kommentare) und enden mit `fail(...)`.
Ein Test wird erst grün, wenn Sie ihn ausgefüllt haben.

## Aufbau

```
demo-unit-tests
├── pom.xml                                  Maven-Konfiguration (JUnit 6, SLF4J + Logback, Jollyday)
└── src
    ├── main/resources/logback.xml           Logging-Konfiguration (Konsole und logs/)
    ├── main/java/com/example                Produktcode (das Testobjekt)
    │   ├── calculator
    │   │   └── Calculator.java              Grundrechenarten für double und BigDecimal
    │   ├── main
    │   │   └── HelloWorld.java              einfaches Programm mit main-Methode („Hallo Welt“)
    │   ├── strings
    │   │   ├── WordSorter.java              sortiert eine Liste von Wörtern
    │   │   └── StringComparator.java        legt fest, wie zwei Wörter verglichen werden
    │   └── tickets
    │       ├── ViennaLinesTicketMachine.java  Fahrscheinautomat der Wiener Linien (Einzelfahrt)
    │       ├── ViennaSchoolHolidays.java      Wiener Schulferien und schulfreie Tage
    │       └── Ticket.java                    der ausgegebene Fahrschein
    └── test/java/com/example                Testcode
        ├── calculator
        │   └── CalculatorTest.java          Beispiel, Aufwärmübung, Übung 2 und Übung 3
        ├── strings
        │   └── WordSorterTest.java          Beispiel und Übung 4
        └── tickets
            └── ViennaLinesTicketMachineTest.java  Übung 5 (legen Sie selbst an)
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

**ViennaLinesTicketMachine** (Tarif der Wiener Linien, Einzelfahrt, Papierfahrschein 2026)

| Fahrgast | Montag–Samstag (Schultag) | Sonntag, gesetzlicher Feiertag, Wiener Schulferien |
| --- | --- | --- |
| unter 6 Jahren | gratis | gratis |
| 6 bis 14 Jahre (bis zum 15. Geburtstag) | 1,60 € (ermäßigt) | **gratis** |
| ab 15 Jahren (auch Senior:innen) | 3,20 € (Vollpreis) | 3,20 € |

- Die Preise stehen als Konstanten `SINGLE_TRIP_FULL_PRICE` und `SINGLE_TRIP_REDUCED_PRICE` in der Klasse.
- Gesetzliche Feiertage in Österreich kommen aus der Bibliothek **Jollyday** (Neujahr, Heilige Drei Könige, Ostermontag, Staatsfeiertag 1. Mai, Christi Himmelfahrt, Pfingstmontag, Fronleichnam, Mariä Himmelfahrt, Nationalfeiertag, Allerheiligen, Mariä Empfängnis, Christtag, Stefanitag). 24.12. und 31.12. sind **keine** gesetzlichen Feiertage.
- Wiener Schulferien und schulfreie Tage (`ViennaSchoolHolidays`, nach dem Schulzeitgesetz berechnet):

| Ferien / schulfreier Tag | Regel | Schuljahr 2026/27 |
| --- | --- | --- |
| Herbstferien | 27.10. – 31.10. | 27.10. – 31.10.2026 |
| Allerseelen | 2.11. | 2.11.2026 |
| Leopolditag | 15.11. | 15.11.2026 |
| Weihnachtsferien | 24.12. – 6.1. (ab 23.12., wenn das ein Montag ist) | 24.12.2026 – 6.1.2027 |
| Semesterferien | erster Montag im Februar bis Sonntag | 1.2. – 7.2.2027 |
| Osterferien | Samstag vor Palmsonntag – Ostermontag | 20.3. – 29.3.2027 |
| Pfingstferien | Samstag vor Pfingsten – Pfingstmontag | 15.5. – 17.5.2027 |
| Sommerferien | Samstag zwischen 28.6. und 4.7. – Sonntag vor dem ersten Montag im September | 3.7. – 5.9.2027 |

Die Methoden im Überblick:

| Methode | Parameter | Rückgabe / Verhalten |
| --- | --- | --- |
| `getSingleTripTicketPrice(int age)` | Alter | Preis nur nach Alter (ohne Gratistage) |
| `getSingleTripTicketPrice(int age, LocalDateTime dateTime)` | Alter, Datum/Uhrzeit | Preis nach Alter und Tag |
| `getSingleTripTicket(int age, BigDecimal payment, LocalDateTime dateTime)` | Alter, bezahlter Betrag, Datum/Uhrzeit | `Ticket` mit ID, Preis, bezahltem Betrag, Wechselgeld, Ausgabedatum, Währung EUR |
| `getSingleTripTicket(int age, BigDecimal payment)` | Alter, bezahlter Betrag | wie oben, mit dem aktuellen Zeitpunkt (`LocalDateTime.now()`) |

Fehlerfälle: negatives Alter, fehlendes Datum (`null`), fehlender Betrag (`null`) oder zu wenig bezahlt lösen eine `IllegalArgumentException` aus.

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

### Übung 5: Den Fahrscheinautomaten der Wiener Linien testen – `ViennaLinesTicketMachineTest`

Legen Sie die Testklasse `src/test/java/com/example/tickets/ViennaLinesTicketMachineTest.java` selbst an
(in IntelliJ: Cursor auf den Klassennamen `ViennaLinesTicketMachine` → `Strg+Shift+T` bzw. `⌘+Shift+T` → „Create New Test…“).
Grundlage sind die Anforderungen oben unter **ViennaLinesTicketMachine**.

Neue Werkzeuge für diese Übung:

| Werkzeug | Wofür | Beispiel |
| --- | --- | --- |
| `@ParameterizedTest` + `@CsvSource` | denselben Test mit vielen Eingaben ausführen | `@CsvSource({"5, 0", "6, 1.60"})` → Methode `void price(int age, String expected)` |
| `@ValueSource` | Test mit einer Liste von Einzelwerten | `@ValueSource(strings = {"2026-11-22T10:00", "2026-12-08T10:00"})` |
| `@Nested` | Tests in Gruppen gliedern | innere Klasse `class PriceByAge { ... }` |
| `LocalDateTime.of(...)` / `LocalDateTime.parse(...)` | festes Datum für den Test | `LocalDateTime.of(2026, 11, 11, 10, 0)` |

Hinweise:

- `BigDecimal` **nicht** mit `assertEquals` vergleichen: `new BigDecimal("0")` und `new BigDecimal("0.00")` sind für `equals` verschieden. Besser: `assertEquals(0, erwartet.compareTo(tatsächlich))`.
- Verwenden Sie immer ein **festes** Datum. Ein Test mit `LocalDateTime.now()` liefert sonntags ein anderes Ergebnis als montags.
- Achten Sie auf **Grenzwerte** (Äquivalenzklassen und Grenzwertanalyse): das letzte Jahr vor und das erste Jahr nach einer Altersgrenze, der letzte Schultag vor und der erste Tag nach Ferienbeginn.

#### 5a: Preis nur nach Alter – `getSingleTripTicketPrice(int age)`

| Nr. | Zu prüfende Logik | Beispiel-Eingabe | Erwartetes Ergebnis |
| --- | --- | --- | --- |
| 1 | Kleinkind fährt gratis | Alter 0 | 0 € |
| 2 | Grenze: letztes Gratis-Jahr | Alter 5 | 0 € |
| 3 | Grenze: erstes ermäßigtes Jahr | Alter 6 | 1,60 € |
| 4 | Grenze: letztes ermäßigtes Jahr | Alter 14 | 1,60 € |
| 5 | Grenze: erster Vollpreis | Alter 15 | 3,20 € |
| 6 | Erwachsener zahlt Vollpreis | Alter 30 | 3,20 € |
| 7 | Kein Seniorentarif für Einzelfahrten | Alter 70 | 3,20 € |
| 8 | Ungültiges Alter | Alter -1 | `IllegalArgumentException` |

#### 5b: Preis nach Alter und Datum – `getSingleTripTicketPrice(int age, LocalDateTime dateTime)`

| Nr. | Zu prüfende Logik | Beispiel-Eingabe | Erwartetes Ergebnis |
| --- | --- | --- | --- |
| 1 | Kind (6–14) an einem normalen Schultag zahlt ermäßigt | Alter 10, Mi 11.11.2026 | 1,60 € |
| 2 | Kind unter 6 fährt auch an Schultagen gratis | Alter 5, Mi 11.11.2026 | 0 € |
| 3 | Erwachsener an einem Schultag zahlt Vollpreis | Alter 30, Mi 11.11.2026 | 3,20 € |
| 4 | **Samstag** ist kein Gratistag | Alter 10, Sa 14.11.2026 | 1,60 € |
| 5 | **Sonntag** ist für Kinder gratis | Alter 10, So 22.11.2026 | 0 € |
| 6 | **Gesetzlicher Feiertag** mit festem Datum | Alter 10, Di 8.12.2026 (Mariä Empfängnis) | 0 € |
| 7 | Nationalfeiertag | Alter 10, Mo 26.10.2026 | 0 € |
| 8 | **Beweglicher Feiertag** (abhängig von Ostern) | Alter 10, Do 14.5.2026 (Christi Himmelfahrt) oder Do 4.6.2026 (Fronleichnam) | 0 € |
| 9 | **Schulfreier Tag** Allerseelen | Alter 10, Mo 2.11.2026 | 0 € |
| 10 | **Schulfreier Tag** Leopolditag (nur Wien) | Alter 10, So 15.11.2026 – besser ein Jahr, in dem der 15.11. ein Werktag ist, z. B. Mi 15.11.2028 | 0 € |
| 11 | **Herbstferien** | Alter 10, Mi 28.10.2026 | 0 € |
| 12 | **Weihnachtsferien** | Alter 10, Mo 28.12.2026 | 0 € |
| 13 | Weihnachtsferien über den Jahreswechsel | Alter 10, Di 5.1.2027 | 0 € |
| 14 | Weihnachtsferien beginnen am 23.12., wenn das ein Montag ist | Alter 10, Mo 23.12.2024 | 0 € |
| 15 | **Semesterferien** | Alter 10, Mi 4.2.2026 | 0 € |
| 16 | **Osterferien** (Montag nach Palmsonntag) | Alter 10, Mo 30.3.2026 | 0 € |
| 17 | **Pfingstferien** (Samstag ist hier gratis!) | Alter 10, Sa 23.5.2026 | 0 € |
| 18 | **Sommerferien** | Alter 10, Mi 12.8.2026 | 0 € |
| 19 | Grenze: letzter Schultag vor den Sommerferien | Alter 10, Fr 3.7.2026 23:59 | 1,60 € |
| 20 | Grenze: erster Ferientag | Alter 10, Sa 4.7.2026 00:00 | 0 € |
| 21 | Grenze: erster Schultag nach den Sommerferien | Alter 10, Mo 7.9.2026 | 1,60 € |
| 22 | Grenze: erster Schultag nach Weihnachten | Alter 10, Do 7.1.2027 | 1,60 € |
| 23 | Grenze 14/15 Jahre an einem Sonntag | Alter 14 bzw. 15, So 22.11.2026 | 0 € bzw. 3,20 € |
| 24 | Erwachsene zahlen auch an Sonn-/Feiertagen und in den Ferien Vollpreis | Alter 30, So 22.11.2026 / Fr 25.12.2026 / Mi 12.8.2026 | 3,20 € |
| 25 | 24.12./31.12. sind keine Feiertage – gratis nur wegen der Weihnachtsferien | Alter 10, Do 31.12.2026 | 0 € |
| 26 | Ungültiges Alter | Alter -1 | `IllegalArgumentException` |
| 27 | Fehlendes Datum | `null` | `IllegalArgumentException` |

#### 5c: Fahrschein kaufen – `getSingleTripTicket(int age, BigDecimal payment, LocalDateTime dateTime)`

| Nr. | Zu prüfende Logik | Beispiel-Eingabe | Erwartetes Ergebnis |
| --- | --- | --- | --- |
| 1 | Wechselgeld wird berechnet | Alter 30, 5,00 €, Schultag | Preis 3,20 €, Wechselgeld 1,80 € |
| 2 | Passender Betrag | Alter 10, 1,60 €, Schultag | Wechselgeld 0 € |
| 3 | Gratis-Fahrschein: ganzer Betrag zurück | Alter 10, 2,00 €, Sonntag | Preis 0 €, Wechselgeld 2,00 € |
| 4 | Zu wenig bezahlt (1 Cent fehlt) | Alter 30, 3,19 € | `IllegalArgumentException` |
| 5 | Kein Betrag | Alter 30, `null` | `IllegalArgumentException` |
| 6 | Ausgabedatum = übergebenes Datum | beliebig | `ticket.getDateOfIssue()` = Eingabe |
| 7 | Bezahlter Betrag wird gespeichert | Alter 30, 5,00 € | `ticket.getAmountPaid()` = 5,00 € |
| 8 | Währung ist Euro | beliebig | `ticket.getCurrency().getCurrencyCode()` = `"EUR"` |
| 9 | Jeder Fahrschein hat eine eigene ID | zwei Käufe | IDs nicht `null` und verschieden |

#### 5d: Zusatz für Schnelle

- Testen Sie `ViennaSchoolHolidays.getSchoolHolidays(2025)` direkt gegen den offiziellen Ferienkalender 2025/26 des Bildungsministeriums.
- Warum ist `getSingleTripTicket(int age, BigDecimal payment)` (ohne Datum) schwer zu testen? Wie könnte man den Code ändern, damit er testbar wird (Stichwort: `java.time.Clock`)?
- Welche Tests müssen Sie anpassen, wenn am 1.1.2027 die Preise steigen (Papierfahrschein 3,30 €)? Wie hilft es, dass die Preise als Konstanten vorliegen?

Erledigt, wenn: alle Fälle aus 5a–5c als Tests umgesetzt und grün sind. Bonus: mindestens ein `@ParameterizedTest`.

## Lizenz

Copyright (c) 2026 Nagarro GmbH, Autor: Florin Balint. Lizenziert unter der
[Apache License 2.0](../LICENSE); Details zu Drittanbieter-Code siehe [NOTICE](../NOTICE).
