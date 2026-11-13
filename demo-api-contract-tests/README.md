# demo-api-contract-tests

Kursprojekt für den Teil **Contract Testing** des Kurses „Einführung in die Testautomatisierung“.

Basis ist die Calculator-API aus [`demo-api-tests`](../demo-api-tests/README.md) – hier aber **nur der
Taschenrechner** (ohne Fahrscheinautomat, ohne Cucumber). Daran lernen Sie
**consumer-driven contract testing** mit [Pact](https://pact.io):

- Der **Consumer** (`CalculatorClient`) schreibt auf, was er von der API erwartet → **Vertrag** (Pact-Datei).
- Der **Provider** (die Calculator-API) beweist, dass er diesen Vertrag einhält.

## Voraussetzungen

| Werkzeug | Version |
| --- | --- |
| JDK | 17 oder neuer |
| Maven | 3.9 oder neuer (in IntelliJ bereits enthalten) |
| Spring Boot | 4.1.2 (WebFlux, Validation) |
| Pact JVM | 4.7.7 (`au.com.dius.pact.consumer:junit5`, `au.com.dius.pact.provider:junit5`) |
| JUnit | 6 (über Spring Boot) |

## Die Beteiligten

| Rolle | Klasse | Name im Vertrag |
| --- | --- | --- |
| Consumer | `src/main/java/com/example/client/CalculatorClient.java` | `calculator-client` |
| 2. Consumer (ab Übung C3) | `src/test/java/com/example/contract/consumer/MobileAppConsumerPactTest.java` | `calculator-mobile-app` |
| Provider | `src/main/java/com/example/api/calculator/CalculatorController.java` | `calculator-api` |

In der Praxis sind Consumer und Provider zwei getrennte Projekte (oft von verschiedenen Teams).
Hier liegen sie zur Vereinfachung in einem Projekt – teilen aber **keine** Klassen: der Consumer hat sein
eigenes `CalculationResult`. Das Einzige, was beide verbindet, ist die Pact-Datei.

## Die zwei Tests

### 1. Consumer-Test – `CalculatorConsumerPactTest`

```
CalculatorClient  ──HTTP──▶  Pact-Mock-Server  (antwortet laut Vertrag)
                                    │
                                    ▼
             target/pacts/calculator-client-calculator-api.json
```

- Jede `@Pact`-Methode beschreibt eine Interaktion (Request → erwartete Antwort).
- Pact startet einen Mock-Server; die echte API läuft **nicht**.
- Der echte `CalculatorClient` wird gegen den Mock-Server getestet.
- Sind alle Tests grün, schreibt Pact den Vertrag nach `target/pacts/`.

Enthaltene Interaktionen:

| Interaktion | Request | erwartete Antwort |
| --- | --- | --- |
| a GET request to add 1 and 2 | `GET /api/calculator/add?numbers=1.0&numbers=2.0` | 200, `{"operation":"ADD","numbers":[…],"result":3.0}` |
| a POST request to add three numbers | `POST /api/calculator/calculate` mit JSON-Body | 200, `result` ist eine Zahl |
| a GET request to add only one number | `GET /api/calculator/add?numbers=1.0` | 400, `application/problem+json` |

### 2. Provider-Test – `CalculatorProviderPactTest`

```
target/pacts/*.json  ──▶  Pact Verifier  ──HTTP──▶  echte Calculator-API (Spring Boot, zufälliger Port)
```

- `@SpringBootTest(webEnvironment = RANDOM_PORT)` startet die echte API.
- `@PactFolder("target/pacts")` lädt den Vertrag.
- Für **jede Interaktion** entsteht ein eigener Testfall: Pact schickt den Request und vergleicht die Antwort.
- `@State("the calculator is available")` bereitet den Provider-Zustand vor (hier: nichts zu tun).

## Tests ausführen

```bash
cd demo-api-contract-tests
mvn clean test
```

Maven führt die Tests in **zwei Schritten** aus (siehe `maven-surefire-plugin` in der `pom.xml`):

1. `default-test` – Consumer-Tests → schreibt `target/pacts/calculator-client-calculator-api.json`
2. `provider-contract-tests` – Provider-Tests → prüft die API gegen diese Datei

**In IntelliJ:** zuerst `CalculatorConsumerPactTest` ausführen, dann `CalculatorProviderPactTest`
(ohne Pact-Datei hat der Provider-Test nichts zu prüfen und schlägt fehl).

> **Der Build ist am Anfang rot – das ist gewollt.** Die Consumer-Tests sind grün, ein Provider-Test
> schlägt fehl. Warum, finden Sie in Übung C1 und C2 heraus.

## Übungen

Vorgehen: **Erst lesen, dann ändern.** Bei Contract Tests steht die Antwort fast immer in der Ausgabe
des Provider-Tests (`Verifying a pact between … Failures: …`) und in der Pact-Datei unter `target/pacts/`.

### Übung C1: Grün und Rot – was ist passiert?

1. `mvn clean test` ausführen (oder in IntelliJ: erst Consumer-, dann Provider-Test).
2. Notieren Sie: Welche Testklassen sind grün, welche rot? Wie viele Testfälle hat der Provider-Test – und warum
   genau so viele?
3. Öffnen Sie `target/pacts/calculator-client-calculator-api.json`. Finden Sie darin die drei Interaktionen
   aus `CalculatorConsumerPactTest` wieder (`description`, `request`, `response`, `matchingRules`).
4. Suchen Sie in der Ausgabe des Provider-Tests die Fehlermeldung. Welche **Interaktion** ist betroffen,
   welches **Feld** (JSON-Pfad), was wurde **erwartet**, was kam **tatsächlich**?

Erledigt, wenn: Sie die rote Interaktion benennen und die Fehlermeldung in eigenen Worten erklären können.

### Übung C2: Den Fehler finden und beheben

| Teil | Aufgabe |
| --- | --- |
| 2a | Beantworten Sie: **Warum ist der Consumer-Test grün, obwohl der Provider-Test rot ist?** Gegen wen hat der Consumer-Test eigentlich getestet? |
| 2b | Wo liegt der Fehler – im Consumer, im Vertrag oder im Provider? Begründen Sie Ihre Antwort. |
| 2c | Den Fehlerzustand im Code finden. Tipp: Gehen Sie den Weg des Requests nach: `CalculatorController` → `Calculator`. Mit `mvn spring-boot:run` und Swagger UI können Sie den Request auch selbst abschicken. |
| 2d | Den Fehler beheben und **alle** Tests erneut ausführen. |
| 2e | Die POST-Interaktion („a POST request to add three numbers“) ruft dieselbe Addition auf – und war trotzdem **grün**. Warum? Vergleichen Sie im Consumer-Test `numberValue("result", 3.0)` mit `numberType("result", 6.0)`. |
| 2f | Auch der Fehlerfall („only one number“) war grün. Warum hat ihn der Fehler nicht betroffen? |

Erledigt, wenn: alle Tests grün sind und Sie benennen können, was hier **Fehlerwirkung** und was
**Fehlerzustand** war – und welcher Test (Unit-Test, API-Test, Contract Test) den Fehler am schnellsten gefunden hätte.

<details>
<summary>Tipps (erst aufklappen, wenn Sie nicht weiterkommen)</summary>

- Der Consumer-Test spricht nur mit dem **Pact-Mock-Server**. Der antwortet genau das, was im Vertrag steht.
  Die echte API und der `Calculator` laufen dabei gar nicht.
- Erst der Provider-Test schickt die aufgezeichneten Requests an die **echte** API.
- Ein Ergebnis von `0.0` bei einer Addition: Mit welchem Startwert beginnt die Schleife, und was passiert
  mit `0`, wenn man es mit etwas **multipliziert**?

</details>

### Übung C3: Ein zweiter Consumer – wer muss sich ändern?

Ein zweites Team baut eine Handy-App (`calculator-mobile-app`) und hat seinen Vertrag geschrieben,
ohne mit dem Provider-Team zu sprechen.

| Teil | Aufgabe |
| --- | --- |
| 3a | In `MobileAppConsumerPactTest` die Zeile `@Disabled(...)` entfernen und `mvn clean test` ausführen. |
| 3b | Welche Tests sind grün, welche rot? Welche Pact-Dateien liegen jetzt in `target/pacts/`? |
| 3c | Lesen Sie die Fehlermeldung. Was erwartet die App, was liefert die API? |
| 3d | Diesmal ist der Provider **nicht** kaputt. Wer muss sich ändern – App oder API? Was würde passieren, wenn die API ihre Antwort für die App ändert? (Denken Sie an `calculator-client`.) |
| 3e | Den Vertrag der App so anpassen, dass alle Tests grün werden. Tipp: Es gibt neben `integerType` auch `decimalType` und `numberType`. |

Erledigt, wenn: alle Tests grün sind und Sie erklären können, warum hier der **Consumer** seinen Vertrag ändern musste.

### Vertiefung (optional)

#### Übung C4: Die anderen Rechenarten in den Vertrag aufnehmen

Erweitern Sie `CalculatorClient` um die fehlenden Methoden (`subtract`, `divide`) und `CalculatorConsumerPactTest`
um je eine `@Pact`-Methode und einen `@Test`. Probieren Sie die Requests vorher in der Swagger UI aus.

| Nr. | Interaktion | Request | Erwartete Antwort |
| --- | --- | --- | --- |
| 1 | subtract | `GET /api/calculator/subtract?numbers=5.0&numbers=3.0` | 200, `operation` = `SUBTRACT`, `result` = 2.0 |
| 2 | multiply | `GET /api/calculator/multiply?numbers=2.0&numbers=3.0&numbers=4.0` | 200, `result` = 24.0 |
| 3 | divide | `GET /api/calculator/divide?numbers=12.0&numbers=3.0&numbers=2.0` | 200, `result` = 2.0 |
| 4 | POST mit jeder `operation` | `POST /api/calculator/calculate` | 200, `operation` wie im Request |

Fragen dazu:
- Wann verwenden Sie einen **exakten Wert** (`numberValue`, `stringValue`), wann einen **Typ-Matcher**
  (`numberType`, `stringType`, `stringMatcher`)? Was bedeutet das für Übung 2e?
- Machen Sie die POST-Interaktion aus `CalculatorConsumerPactTest` strenger, sodass sie den Fehler aus
  Übung C2 ebenfalls gefunden hätte. Bauen Sie den Fehler zur Probe kurz wieder ein.

#### Übung C5: Fehlerfälle in den Vertrag aufnehmen

Alle Fehler kommen als HTTP 400 im Format `application/problem+json` (RFC 9457). Nehmen Sie die Fälle
in den Vertrag auf, die ein Consumer wirklich behandeln muss:

| Nr. | Fehlerfall | Request | Erwartet |
| --- | --- | --- | --- |
| 1 | subtract mit drei Zahlen | `GET /api/calculator/subtract?numbers=5.0&numbers=3.0&numbers=1.0` | **400**, `detail` = `Exactly two arguments must be provided for subtraction!` |
| 2 | Parameter fehlt | `GET /api/calculator/add` | **400** |
| 3 | keine Zahl | `GET /api/calculator/add?numbers=abc&numbers=1` | **400** |
| 4 | unbekannte Operation | POST `{"operation": "POWER", "numbers": [1, 2]}` | **400** |
| 5 | Pflichtfeld fehlt | POST `{"operation": "ADD"}` | **400** |
| 6 | Division durch 0 | `GET /api/calculator/divide?numbers=1.0&numbers=0.0` | Erst vermuten, dann in der Swagger UI ausprobieren: Statuscode? Welcher **JSON-Typ** hat `result`? |

Fragen dazu:
- Für Nr. 2–5 liefert die API je eine andere `detail`-Meldung. Sollte der Vertrag den genauen Text
  festschreiben oder nur `stringType`? Was passiert, wenn das Provider-Team die Meldung umformuliert?
- Nr. 6: Passt die Antwort zu `numberType("result", …)`? Was bedeutet das für einen Consumer, der
  `result` als Zahl erwartet? Ist das ein Fehler im Provider, im Vertrag oder „gewolltes Verhalten“?

#### Übung C6: Zum Nachdenken

- Was prüft ein **Contract Test**, was ein **API-Test** (`demo-api-tests`) nicht prüft – und umgekehrt?
  Hätte `CalculatorAdditionApiTest` den Fehler aus Übung C2 gefunden?
- Warum heißt es **consumer-driven**? Wer schreibt den Vertrag, wer muss ihn erfüllen?
- Provider States: Was müsste in einer `@State`-Methode passieren, wenn die API Daten aus einer Datenbank liefert?
- In echten Projekten liegen die Pact-Dateien nicht in `target/`, sondern in einem **Pact Broker**.
  Recherchieren Sie: Was macht `can-i-deploy`?

## API starten (optional)

```bash
mvn spring-boot:run
```

Swagger UI: <http://localhost:8081/swagger-ui.html> (Port **8081**, damit `demo-api-tests` parallel auf 8080 laufen kann).
