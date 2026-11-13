# demo-taf-tests

Course project for the part **test automation frameworks (TAF)** of the course
"Einführung in die Testautomatisierung" (introduction to test automation).

[`demo-ui-tests`](../demo-ui-tests/README.md) showed how a single test class controls a browser. In a real
project there are hundreds of test cases, several applications and test data from different sources.
A **test automation framework** organises this in **layers**, so that:

- test cases are **short and readable** – they describe *what* is tested, not *how*,
- a change on the website (e.g. a new ID for a button) is fixed in **one place**, not in every test,
- test data can come from **different sources** (database, service, file, …) without the test caring.

The example application is <https://www.base64encode.org/> (and its sister site
<https://www.base64decode.org/>): text is encoded to Base64 and decoded back.

## Prerequisites

| Tool | Version |
| --- | --- |
| JDK | 17 or newer |
| Maven | 3.9 or newer (already included in IntelliJ) |
| Google Chrome | current version, installed normally |
| Selenium | 4.52.0 – downloaded automatically by Maven (ChromeDriver via Selenium Manager) |
| JUnit | 6.1.3 |
| Internet | yes – the tests open base64encode.org and base64decode.org |

## Running the tests

**In IntelliJ:** open a class in `src/test/java/com/example/taf/tests` and click the green ▶.

**In the terminal:**

```bash
cd demo-taf-tests
mvn clean test                              # with a visible browser window (default)
mvn clean test -Dbrowser.headless=true      # without a window – the decode tests are skipped
```

> **At the start the decode tests are red – that is intended.** They are exercise 2 and end with `fail(...)`.
>
> **Why are the decode tests skipped in headless mode?** base64decode.org shows a bot check
> ("Just a moment...") to headless browsers. The tests do not try to get around it – they are skipped
> with a message (JUnit `Assumptions`) and run normally with a visible browser.

## The layers

```
 Test case            Base64EncodeTest, Base64DecodeTest                       src/test/java
 ───────────────────────────────────────────────────────────────────────────────────────────
 Base test class      BaseTafTest       → base64App(), dbClient(), serviceDataClient()
 Layer 4: app         Base64App         → base64Keywords()
 Layer 3: keywords    Base64Keywords    → openBase64Site(), closeCookies(), encodeText(...)     src/main/java
 Layer 2: pages       Base64EncodePage, Base64DecodePage, CookieConsentDialog  (locators)
 Layer 1: core        UiSession         → clickElement, type, waitForVisible, … (Selenium)
 Config               TafConfig         ← taf.properties                       src/main/resources
 ───────────────────────────────────────────────────────────────────────────────────────────
 Data clients         DBClient, ServiceDataClient  → test data for the test cases
```

Each layer only talks to the layer directly below it:

| Layer | Class(es) | Knows about | Does **not** know about |
| --- | --- | --- | --- |
| Test case | `Base64EncodeTest`, … | keywords, data clients, expected results | Selenium, locators, URLs |
| Base test class | `BaseTafTest` | which app and which clients exist; browser start/stop | single test steps |
| App | `Base64App` | which keyword sets the application has | how a keyword works |
| Keywords | `Base64Keywords` | business actions ("encode this text") | locators, Selenium |
| Pages | `Base64EncodePage`, … | locators and page behaviour (e.g. "live mode") | test data, assertions |
| Core | `UiSession` | Selenium, waiting, clicking | any specific website |
| Config | `TafConfig` + `taf.properties` | URLs, browser settings, timeouts | – |

**The framework lives in `src/main`, the test cases in `src/test`.** That is on purpose: the framework
is a product of its own that could be packaged as a JAR and used by several test projects. That is also
why Selenium and the JUnit API are normal (compile) dependencies in the `pom.xml`.

### A test case

```java
class Base64EncodeTest extends BaseTafTest {

    @Test
    void encodeTextFromDatabase() {
        base64App().base64Keywords().openBase64Site();

        base64App().base64Keywords().closeCookies();

        String textToEncode = dbClient().getTestText("UMLAUTS").text();
        String actualResult = base64App().base64Keywords().encodeText(textToEncode);

        // Java Base64 conversion
        String expectedResult = Base64.getEncoder().encodeToString(textToEncode.getBytes(StandardCharsets.UTF_8));

        Assertions.assertEquals(expectedResult, actualResult);
    }
}
```

No locator, no `driver`, no waiting – only keywords and a data client. The **expected result** is calculated
independently with Java's own `Base64` encoder (a **test oracle**): the website must produce the same.

## Data clients

Test data often does not come from the test itself, but from other systems. Each source gets a
**client**. The test only asks the client for data – where the data comes from is the client's business.

| Client | Simulates | Delivers | Used for |
| --- | --- | --- | --- |
| `DBClient` | a database table `TEST_TEXTS` (in a real project: JDBC + SQL) | `TestText(id, name, text)` | texts to **encode** |
| `ServiceDataClient` | a REST service `GET /api/base64-samples` (in a real project: HTTP + JSON) | `EncodedSample(name, base64, expectedText)` | samples to **decode**, with the expected result |

Both clients are **simulated** – there is no real database or service, the data is in a list in the class.
Replacing the list with a real JDBC or HTTP call later would not change a single test case.

```java
String text = dbClient().getTestText("UMLAUTS").text();                       // from the "database"
EncodedSample sample = serviceDataClient().getEncodedSample("HELLO_WORLD");   // from the "service"
```

## Configuration – `src/main/resources/taf.properties`

| Key | Default | Meaning |
| --- | --- | --- |
| `base64.encode.url` | `https://www.base64encode.org/` | address of the encode site |
| `base64.decode.url` | `https://www.base64decode.org/` | address of the decode site |
| `browser.headless` | `false` | `true` = no visible browser window |
| `browser.window.width` / `.height` | `1400` / `1000` | window size |
| `browser.timeout.seconds` | `10` | how long the framework waits at most for an element |

Every value can be overridden on the command line without changing a file: `mvn test -Dbrowser.headless=true`.
This is how the same tests run against a test system and a staging system (only the URLs differ).

## Things worth knowing about the website

- **Cookie dialog:** both sites show "We value your privacy" (Quantcast). `closeCookies()` clicks **AGREE**
  and waits until the dialog is gone.
- **Live mode instead of the ENCODE/DECODE button:** the button sends the form to the server, and
  base64decode.org answers automated browsers with a bot check. In **live mode** the page converts while
  you type – no form is sent. Because the result changes with every key stroke, the page object waits
  until the result is **stable** before reading it. See `Base64ConverterPage.convert(...)`.
- **Reading form fields:** the result is in a `<textarea>`. Its content is the `value` property
  (`UiSession.getValue(...)`), not `getText()`.

## Tests in this project

| Class | Test | Data from |
| --- | --- | --- |
| `Base64EncodeTest` | `encodeTextFromDatabase` – the example above (text with umlauts) | `DBClient` |
| `Base64DecodeTest` | `decodeSampleFromService` – exercise 2 | `ServiceDataClient` |
| | `encodeAndDecodeRoundTrip` – exercise 2 | `DBClient` |

## Exercises

In a framework, a new test often needs something the framework cannot do yet. Then you extend it
**layer by layer, from the bottom up**: first the page object (locators), then the keyword, then the
test case. The test case itself stays as short as `encodeTextFromDatabase`.

### Exercise 1: Encode with rejected cookies

Repeat the test `encodeTextFromDatabase` – but this time **reject** the cookies instead of agreeing.

| Part | Task |
| --- | --- |
| 1.1 | Open base64encode.org in Chrome and find out how to reject the cookies. (Hint: the first screen of the dialog only has **MORE OPTIONS** and **AGREE**.) Use the DevTools to find the IDs of the buttons you need. |
| 1.2 | **Layer 2 – page:** add a method `rejectAll()` to `CookieConsentDialog`. Use `session.clickElement(...)` and wait until the dialog is closed (look at `agree()`). |
| 1.3 | **Layer 3 – keyword:** add a keyword `rejectCookies()` to `Base64Keywords` that calls the new page method. |
| 1.4 | **Test case:** write `encodeTextFromDatabaseWithRejectedCookies` in `Base64EncodeTest`. Use another text from the database, e.g. `dbClient().getTestText("JSON")`. |
| 1.5 | Compare your test case with `encodeTextFromDatabase`. Which lines are different? Which layers did you have to touch – and which not? |

Done when: both tests in `Base64EncodeTest` are green, and the new test case contains no locator and no Selenium code.

### Exercise 2: Test the decoding – `Base64DecodeTest`

The two test methods are prepared: the test data is already fetched from the clients, the rest is
missing (see the `TODO`s). All keywords you need already exist in `Base64Keywords`.

| Part | Task |
| --- | --- |
| 2.1 | `decodeSampleFromService`: open base64decode.org, close the cookies, decode `sample.base64()` and check that the result is `sample.expectedText()`. |
| 2.2 | Look into `ServiceDataClient`: which other samples does the service deliver? Run the test with the sample `UMLAUTS` as well. |
| 2.3 | `encodeAndDecodeRoundTrip`: encode `originalText` on base64encode.org, decode the result on base64decode.org and check that you get `originalText` back. |
| 2.4 | In 2.1 the expected result comes from the **service**, in 2.3 from the **original text** itself. In `Base64EncodeTest` it is calculated with Java's `Base64` encoder. What are the pros and cons of each kind of **test oracle**? |
| 2.5 | Run the tests with `-Dbrowser.headless=true`. What happens to the decode tests, and why? |

Done when: both tests in `Base64DecodeTest` are green (with a visible browser) and you can explain where the
test data and the expected results come from.

## License

Copyright (c) 2026 Nagarro GmbH, author: Florin Balint. Licensed under the
[Apache License 2.0](../LICENSE); for third-party code see [NOTICE](../NOTICE).
