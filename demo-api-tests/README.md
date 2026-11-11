# demo-api-tests

Kursprojekt für den Teil **API-Tests** des Kurses „Einführung in die Testautomatisierung“.

Die Klassen aus [`demo-unit-tests`](../demo-unit-tests/README.md) (`Calculator`, `WordSorter`,
`ViennaLinesTicketMachine`, `ViennaSchoolHolidays`) sind hier unverändert enthalten. Zusätzlich macht
eine **Spring-Boot-Anwendung** den Taschenrechner und den Fahrscheinautomaten über eine **REST-API**
erreichbar. So lernen Sie:

- was eine REST-API ist (URL, HTTP-Methode, Parameter, JSON, Statuscode),
- wie man eine API im Browser über **Swagger UI** ausprobiert,
- wie man API-Tests mit **JUnit 6** und dem **WebTestClient** schreibt.

## Voraussetzungen

| Werkzeug | Version |
| --- | --- |
| JDK | 17 oder neuer |
| Maven | 3.9 oder neuer (in IntelliJ bereits enthalten) |
| Spring Boot | 4.1.2 (WebFlux, Actuator, Validation) – wird von Maven automatisch geladen |
| springdoc-openapi | 3.2.0 (OpenAPI-Beschreibung und Swagger UI) |
| Jollyday | 2.24.0 (österreichische Feiertage) |
| JUnit | 6 (über Spring Boot) |

## API starten

**In IntelliJ:** Ordner `demo-api-tests` öffnen (File → Open → `pom.xml` → „Open as Project“), dann die Klasse
`src/main/java/com/example/api/DemoApiApplication.java` öffnen und auf das grüne ▶ neben `main` klicken.

**Im Terminal:**

```bash
cd demo-api-tests
mvn spring-boot:run
```

Die API läuft, wenn in der Konsole `Started DemoApiApplication in ... seconds` steht.
Beenden mit `Strg+C` (bzw. dem roten ■ in IntelliJ).

Port 8080 belegt? Dann mit einem anderen Port starten:
`mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081`

## Wichtige Adressen

| Adresse | Was ist das? |
| --- | --- |
| <http://localhost:8080/> | leitet direkt zur Swagger UI weiter |
| <http://localhost:8080/swagger-ui.html> | **Swagger UI**: alle Endpunkte ansehen und im Browser ausprobieren |
| <http://localhost:8080/v3/api-docs> | OpenAPI-Beschreibung der API als JSON (maschinenlesbar) |
| <http://localhost:8080/health> | **Health-Seite**: `{"status":"UP","message":"Demo API is up and running", ...}` |
| <http://localhost:8080/actuator/health> | technischer Health-Check von Spring Boot Actuator (mit Details) |
| <http://localhost:8080/actuator/info> | Infos zur Anwendung (Name, Firma, Autor) |

### Swagger UI verwenden

1. <http://localhost:8080/swagger-ui.html> öffnen.
2. Einen Endpunkt aufklappen, z. B. **Calculator → GET /api/calculator/add**.
3. **„Try it out“** klicken, Werte eintragen (für `numbers` mit „Add item“ mehrere Zahlen anlegen).
4. **„Execute“** klicken.
5. Darunter sehen Sie: den erzeugten `curl`-Befehl, die **Request-URL**, den **Statuscode** und den **Response-Body**.

### Alternative: IntelliJ HTTP Client

Die Datei [`requests.http`](requests.http) enthält fertige Requests für alle Endpunkte.
In IntelliJ öffnen und auf das grüne ▶ neben einem Request klicken.

## Endpunkte

### Calculator

| Methode | URL | Beispiel | Ergebnis |
| --- | --- | --- | --- |
| GET | `/api/calculator/add?numbers=…` | `?numbers=1&numbers=2` | `{"operation":"ADD","numbers":[1.0,2.0],"result":3.0}` |
| GET | `/api/calculator/subtract?numbers=…` | `?numbers=5&numbers=3` (genau zwei Zahlen) | `result: 2.0` |
| GET | `/api/calculator/multiply?numbers=…` | `?numbers=2&numbers=3&numbers=4` | `result: 24.0` |
| GET | `/api/calculator/divide?numbers=…` | `?numbers=12&numbers=3&numbers=2` | `result: 2.0` |
| POST | `/api/calculator/calculate` | Body `{"operation":"ADD","numbers":[1,2,3]}` | `result: 6.0` |

`operation` kann `ADD`, `SUBTRACT`, `MULTIPLY` oder `DIVIDE` sein.

### Fahrscheinautomat der Wiener Linien

| Methode | URL | Beispiel | Ergebnis |
| --- | --- | --- | --- |
| GET | `/api/tickets/price?age=…&dateTime=…` | `?age=10&dateTime=2026-11-11T10:00:00` | `{"age":10,"dateTime":"2026-11-11T10:00:00","price":1.60,"currency":"EUR"}` |
| POST | `/api/tickets` | Body `{"age":30,"payment":5.00,"dateTime":"2026-11-11T10:00:00"}` | **201 Created**, Ticket mit `id`, `price` 3.20, `change` 1.80, ... |
| GET | `/api/school-holidays/{startYear}` | `/api/school-holidays/2026` | Liste aller Wiener Schulferien im Schuljahr 2026/27 |
| GET | `/api/school-holidays/check?date=…` | `?date=2026-12-28` | `{"date":"2026-12-28","schoolHoliday":true}` |

`dateTime` ist optional – ohne Angabe wird der aktuelle Zeitpunkt verwendet.
Preisregeln: unter 6 gratis; 6–14 Jahre 1,60 €, an Sonn- und Feiertagen sowie in den Wiener Schulferien gratis;
ab 15 Jahren 3,20 € (Details siehe [README von demo-unit-tests](../demo-unit-tests/README.md)).

### Health

| Methode | URL | Ergebnis |
| --- | --- | --- |
| GET | `/health` | `{"status":"UP","message":"Demo API is up and running","timestamp":"...","uptimeSeconds":42}` |
| GET | `/actuator/health` | `{"status":"UP", "components": {...}}` |

### Fehlerantworten

Ungültige Eingaben liefern **400 Bad Request** mit einem JSON-Body im Standardformat „Problem Details“ (RFC 9457):

```json
{
  "title": "Bad Request",
  "status": 400,
  "detail": "At least two arguments must be provided!",
  "instance": "/api/calculator/add"
}
```

Beispiele: nur eine Zahl bei `add`, fehlender oder nicht numerischer Parameter (`numbers=abc`),
zu wenig bezahlt beim Ticketkauf (`"detail": "Insufficient payment"`), fehlendes Pflichtfeld im JSON-Body.

## Asynchron und nicht blockierend

Die API verwendet **Spring WebFlux** auf dem Server **Netty** statt des klassischen Spring MVC auf Tomcat:

- Alle Controller-Methoden geben ein **`Mono<T>`** (genau ein Ergebnis) oder einen **`Flux<T>`** (Strom von Ergebnissen)
  zurück. Das ist ein „Versprechen“ auf ein Ergebnis: Die Methode kehrt sofort zurück, das Ergebnis wird
  geschrieben, sobald es berechnet ist.
- Netty bedient viele Anfragen mit wenigen Threads (Event Loop). Kein Thread wartet untätig auf ein Ergebnis.
- Arbeit, die blockieren könnte (z. B. Jollyday liest Feiertagsdateien), wird mit
  `subscribeOn(Schedulers.boundedElastic())` auf einen eigenen Thread-Pool verlagert, damit der Event Loop frei bleibt.
- `Mono.fromCallable(...)` sorgt dafür, dass eine Exception (z. B. `IllegalArgumentException`) als Fehlersignal
  weitergereicht und vom `ApiExceptionHandler` in eine 400-Antwort umgewandelt wird.

Für die Tests ändert das nichts: Der `WebTestClient` wartet automatisch auf die Antwort.

## Aufbau

```
demo-api-tests
├── pom.xml                                   Maven-Konfiguration (Spring Boot 4.1.2, springdoc, Jollyday)
├── requests.http                             Beispiel-Requests für den IntelliJ HTTP Client
└── src
    ├── main/resources
    │   ├── application.yml                   Konfiguration (Port, Actuator, Swagger UI)
    │   └── logback.xml                       Logging-Konfiguration (Konsole und logs/)
    ├── main/java/com/example                 Produktcode aus demo-unit-tests (unverändert)
    │   ├── Calculator.java, WordSorter.java, StringComparator.java
    │   ├── tickets/                          ViennaLinesTicketMachine, ViennaSchoolHolidays, Ticket
    │   └── api                               die REST-API
    │       ├── DemoApiApplication.java       Startklasse (main)
    │       ├── calculator/                   CalculatorController + Request/Response
    │       ├── tickets/                      TicketController, SchoolHolidayController + Request/Response
    │       ├── health/HealthController.java  /health und Weiterleitung von / zur Swagger UI
    │       ├── error/ApiExceptionHandler.java  IllegalArgumentException -> 400 Bad Request
    │       └── config/                       Beans der Fachklassen, Swagger-Infos
    └── test/java/com/example/api
        ├── calculator/CalculatorAdditionApiTest.java  zwei Beispiel-API-Tests (GET und POST) – hier ergänzen Sie Ihre Tests
        └── health/HealthApiTest.java                  Smoke-Tests für /health und /actuator/health
```

## Tests ausführen

```bash
# im Ordner demo-api-tests
mvn test

# oder im übergeordneten Ordner testing-pro-2026 (über die Parent-POM)
mvn -pl demo-api-tests test
```

In IntelliJ: Rechtsklick auf `src/test/java/com/example/api` → „Run 'Tests in 'com.example.api''“.

Beim ersten Start laufen **4 Tests**, alle grün: die zwei Beispiele in `CalculatorAdditionApiTest` und die
zwei Smoke-Tests in `HealthApiTest`. Alle weiteren Tests schreiben Sie selbst (siehe Übungen).

### So funktioniert ein API-Test

Vorlage ist [`CalculatorAdditionApiTest`](src/test/java/com/example/api/calculator/CalculatorAdditionApiTest.java)
mit zwei fertigen Beispielen:

| Beispieltest | Was zeigt er? |
| --- | --- |
| `addTwoNumbers` | GET mit Query-Parametern, Statuscode 200, Content-Type JSON, mehrere Felder per `jsonPath` |
| `addWithJsonBody` | POST mit JSON-Body |

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // startet die ganze App
@AutoConfigureWebTestClient                                                  // stellt den WebTestClient bereit
class CalculatorAdditionApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void addTwoNumbers() {
        webTestClient.get()                                        // Arrange: HTTP-Methode
                .uri("/api/calculator/add?numbers=1&numbers=2")   //          URL mit Parametern
                .exchange()                                       // Act:     Request abschicken
                .expectStatus().isOk()                            // Assert:  Statuscode 200
                .expectBody()
                .jsonPath("$.result").isEqualTo(3.0);             //          Wert im JSON
    }
}
```

`@SpringBootTest(webEnvironment = RANDOM_PORT)` startet die komplette Anwendung auf einem freien Port – so,
als hätten Sie `mvn spring-boot:run` ausgeführt. Der `WebTestClient` schickt echte HTTP-Requests und prüft die Antwort.
Sie müssen die API für die Tests also **nicht** vorher selbst starten.

### Werkzeugkasten: WebTestClient

| Baustein | Wofür |
| --- | --- |
| `.get()`, `.post()`, `.put()`, `.delete()` | HTTP-Methode |
| `.uri("/pfad?param=wert")` | URL mit Query-Parametern |
| `.uri(b -> b.path("/pfad").queryParam("numbers", 1, 2, 3).build())` | URL im Code zusammenbauen (praktisch bei variablen Werten) |
| `.contentType(MediaType.APPLICATION_JSON).bodyValue("{...}")` | JSON-Body als Text senden |
| `.bodyValue(new CalculationRequest(...))` | JSON-Body als Java-Objekt senden |
| `.expectStatus().isOk()` / `.isCreated()` / `.isBadRequest()` | Statuscode prüfen (200 / 201 / 400) |
| `.expectHeader().contentType(MediaType.APPLICATION_JSON)` | Header prüfen |
| `.expectBody().jsonPath("$.feld").isEqualTo(...)` | einzelnen JSON-Wert prüfen |
| `.jsonPath("$.liste.length()").isEqualTo(3)` / `.jsonPath("$[0].name")` | Listen im JSON prüfen |
| `.jsonPath("$.feld").exists()` / `.doesNotExist()` | Feld vorhanden / nicht vorhanden |
| `.expectBody(Klasse.class).returnResult().getResponseBody()` | Antwort als Java-Objekt, danach z. B. mit AssertJ `assertThat(...)` prüfen |

Hinweise:

- Ein Fehler wird **nicht** als Exception sichtbar, sondern als **Statuscode 400** mit JSON-Body
  (`$.status`, `$.detail`, siehe „Fehlerantworten“ oben).
- Kommazahlen: `0.1 + 0.2` ergibt bei `double` nicht genau `0.3`. Vergleichen Sie mit Toleranz, z. B.
  `.jsonPath("$.result").value(Double.class, r -> assertThat(r).isCloseTo(0.3, within(1e-9)))`.
- Für viele ähnliche Fälle eignet sich `@ParameterizedTest` mit `@CsvSource` (wie in `demo-unit-tests`).
- Ticketpreise: Verwenden Sie immer ein **festes** `dateTime`, sonst hängt das Ergebnis vom heutigen Tag ab.

## Übungen

Vorgehen: Request zuerst in der **Swagger UI** (oder `requests.http`) ausprobieren, die Antwort ansehen,
dann den Test schreiben. Jeder Test folgt dem Muster **Arrange – Act – Assert**.

### Übung A1: API kennenlernen

1. API starten, Swagger UI öffnen und jeden Endpunkt einmal ausprobieren.
2. Welche Statuscodes sehen Sie (200, 201, 400)? Wann kommt welcher?
3. `/health` und `/actuator/health` aufrufen. Worin unterscheiden sie sich?

### Übung A2: Addition testen – `CalculatorAdditionApiTest`

Ergänzen Sie die Testklasse um folgende Tests:

| Nr. | Zu prüfen | Request | Erwartet |
| --- | --- | --- | --- |
| 1 | drei Zahlen addieren | `GET /api/calculator/add?numbers=1&numbers=2&numbers=3` | 200, `result` = 6.0 |
| 2 | URL mit `UriBuilder` bauen | wie Nr. 1, aber mit `.uri(b -> b.path(...).queryParam(...).build())` | 200, `result` = 6.0 |
| 3 | negative Zahlen und Kommazahlen | z. B. `-1.5` + `-2.5`, `2.5` + `0.25`, `-1` + `1` (als `@ParameterizedTest`) | -4.0 / 2.75 / 0.0 |
| 4 | Antwort als Java-Objekt | `expectBody(CalculationResponse.class)` | `operation` = ADD, `numbers` = [10.0, 20.0], `result` = 30.0 |
| 5 | Rundungsfehler bei `double` | `?numbers=0.1&numbers=0.2` | Ergebnis ≈ 0.3 (mit Toleranz prüfen) |
| 6 | POST mit Java-Objekt | `.bodyValue(new CalculationRequest(CalculationOperation.ADD, List.of(4.0, 5.0)))` | 200, `result` = 9.0 |
| 7 | nur eine Zahl | `?numbers=5` | **400**, `detail` = `At least two arguments must be provided!` |
| 8 | Parameter fehlt | `GET /api/calculator/add` | **400** |
| 9 | keine Zahl | `?numbers=abc&numbers=1` | **400** |
| 10 | Pflichtfeld fehlt im Body | POST `{"operation": "ADD"}` | **400** |

Erledigt, wenn: alle Tests grün sind und Sie erklären können, warum Nr. 7 und Nr. 8 zwar denselben
Statuscode, aber unterschiedliche Fehlermeldungen liefern.

### Übung A3: Die anderen Rechenarten testen

Legen Sie nach dem Vorbild eine eigene Testklasse an (z. B. `CalculatorApiTest`) und testen Sie:

| Nr. | Zu prüfen | Erwartet |
| --- | --- | --- |
| 1 | `subtract` mit zwei Zahlen (`5 - 3`) | 200, `result` = 2.0 |
| 2 | `subtract` mit drei Zahlen | **400**, `detail` = `Exactly two arguments must be provided for subtraction!` |
| 3 | `multiply` (`2 * 3 * 4`) | 200, `result` = 24.0 |
| 4 | `divide` von links nach rechts (`12 / 3 / 2`) | 200, `result` = 2.0 |
| 5 | `divide` durch 0 (`1 / 0`) | Erst vermuten, dann ausprobieren: Welcher Statuscode, welcher `result`? |
| 6 | POST `/calculate` mit jeder `operation` | passendes Ergebnis |
| 7 | POST mit unbekannter `operation` (z. B. `"POWER"`) | **400** |

### Übung A4: Ticketpreise testen – `TicketApiTest`

Testen Sie `GET /api/tickets/price?age=…&dateTime=…`. Die Fälle kennen Sie aus Übung 5 in
[demo-unit-tests](../demo-unit-tests/README.md) – jetzt prüfen Sie sie über die API:

| Nr. | Zu prüfen | Beispiel | Erwartet (`$.price`) |
| --- | --- | --- | --- |
| 1 | Altersgrenze 5/6 an einem Schultag | Alter 5 bzw. 6, `2026-11-11T10:00:00` | 0 bzw. 1.6 |
| 2 | Altersgrenze 14/15 an einem Schultag | Alter 14 bzw. 15 | 1.6 bzw. 3.2 |
| 3 | Samstag ist kein Gratistag | Alter 10, `2026-11-14T10:00:00` | 1.6 |
| 4 | Kind am Sonntag | Alter 10, `2026-11-22T10:00:00` | 0 |
| 5 | Kind am Feiertag | Alter 10, `2026-12-08T10:00:00` | 0 |
| 6 | Kind in den Ferien | Alter 10, `2026-08-12T10:00:00` | 0 |
| 7 | Erwachsener am Sonntag | Alter 30, `2026-11-22T10:00:00` | 3.2 |
| 8 | Währung | beliebig | `$.currency` = `EUR` |
| 9 | negatives Alter | Alter -1 | **400** |
| 10 | ungültiges Datum | `dateTime=gestern` | **400** |

### Übung A5: Ticket kaufen – `POST /api/tickets`

| Nr. | Zu prüfen | Body | Erwartet |
| --- | --- | --- | --- |
| 1 | Kauf mit Wechselgeld | `{"age": 30, "payment": 5.00, "dateTime": "2026-11-11T10:00:00"}` | **201**, `price` 3.2, `change` 1.8, `currency` EUR |
| 2 | Ticket hat eine ID | wie Nr. 1 | `$.id` existiert |
| 3 | zwei Käufe, zwei IDs | zweimal Nr. 1 | IDs verschieden |
| 4 | Ausgabedatum | wie Nr. 1 | `$.dateOfIssue` = `2026-11-11T10:00:00` |
| 5 | Kind am Sonntag | `{"age": 10, "payment": 2.00, "dateTime": "2026-11-22T10:00:00"}` | `price` 0, `change` 2.0 |
| 6 | zu wenig bezahlt | `{"age": 30, "payment": 1.00}` | **400**, `detail` = `Insufficient payment` |
| 7 | Pflichtfeld fehlt | `{"payment": 5.00}` | **400** |

### Übung A6: Schulferien testen – `/api/school-holidays`

| Nr. | Zu prüfen | Request | Erwartet |
| --- | --- | --- | --- |
| 1 | Anzahl der Einträge | `GET /api/school-holidays/2026` | `$.length()` = 8 |
| 2 | erster Eintrag | wie Nr. 1 | `$[0].name` = `Herbstferien`, `$[0].start` = `2026-10-27` |
| 3 | Sommerferien 2027 | wie Nr. 1 | letzter Eintrag: `2027-07-03` bis `2027-09-05` |
| 4 | Ferientag | `GET /api/school-holidays/check?date=2026-12-28` | `$.schoolHoliday` = true |
| 5 | Schultag | `GET /api/school-holidays/check?date=2026-11-11` | `$.schoolHoliday` = false |

Zusatz: Vergleichen Sie `GET /api/school-holidays/2025` mit dem offiziellen Ferienkalender 2025/26 des Bildungsministeriums.

### Übung A7: Zum Nachdenken

- Welche Tests sind **Unit-Tests**, welche **API-Tests**? Was prüft ein API-Test zusätzlich
  (URL, HTTP-Methode, JSON, Statuscodes, Fehlerbehandlung)?
- Welche Tests laufen schneller? Warum?
- Welche Fälle würden Sie als Unit-Test und welche als API-Test schreiben, wenn Sie nur wenig Zeit haben?

## Lizenz

Copyright (c) 2026 Nagarro GmbH, Autor: Florin Balint. Lizenziert unter der
[Apache License 2.0](../LICENSE); Details zu Drittanbieter-Code siehe [NOTICE](../NOTICE).
