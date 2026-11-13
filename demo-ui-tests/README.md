# demo-ui-tests

Kursprojekt für den Teil **UI-Tests** des Kurses „Einführung in die Testautomatisierung“.

Hier steuert ein Test einen **echten Browser** (Google Chrome) – genau so, wie ein Mensch die Website
bedienen würde: Seite öffnen, Cookie-Banner wegklicken, Links anklicken, Text lesen. Als Beispiel dient
die öffentliche Website <https://www.penny.at/>. Sie lernen:

- wie man mit **Selenium WebDriver** einen Browser fernsteuert,
- wie man Elemente findet (**Locator**: CSS-Selektor, ID, …) und mit ihnen interagiert,
- warum man bei UI-Tests **warten** muss – und wie man es richtig macht (`WebDriverWait`).

## Voraussetzungen

| Werkzeug | Version |
| --- | --- |
| JDK | 17 oder neuer |
| Maven | 3.9 oder neuer (in IntelliJ bereits enthalten) |
| Google Chrome | aktuelle Version, ganz normal installiert |
| Selenium | 4.52.0 – wird von Maven automatisch geladen |
| JUnit | 6.1.3 |
| AssertJ | 3.27.7 (lesbare Prüfungen, z. B. `assertThat(titel).startsWith("…")`) |
| Internet | ja – die Tests öffnen www.penny.at |

**ChromeDriver müssen Sie nicht installieren.** Selenium bringt den **Selenium Manager** mit: Beim ersten
Testlauf erkennt er Ihre Chrome-Version, lädt den passenden ChromeDriver herunter und legt ihn in
`~/.cache/selenium` ab. Der erste Start dauert deshalb ein paar Sekunden länger.

## Tests ausführen

**In IntelliJ:** `src/test/java/com/example/ui/penny/PennyImpressumTest.java` öffnen und auf das grüne ▶ neben
der Klasse oder einem Test klicken. Ein Chrome-Fenster geht auf, und Sie sehen, was der Test tut.

**Im Terminal:**

```bash
cd demo-ui-tests
mvn clean test                    # mit sichtbarem Browserfenster
mvn clean test -Dheadless=true    # ohne Fenster (schneller, z. B. für CI-Server)
```

> **Am Anfang ist ein Test rot – das ist gewollt.** `impressumPageHasTitleImpressum` ist Übung U1 und
> endet mit `fail(...)`.

## Projektstruktur

```
demo-ui-tests
└── src/test/java/com/example/ui
    ├── base
    │   └── BaseUiTest.java             Basisklasse: Browser starten/schließen, clickElement, Warten, …
    └── penny
        └── PennyImpressumTest.java     Beispiel 1, Beispiel 2 und Übung U1 (erbt von BaseUiTest)
```

Jede Website bekommt ein eigenes Package (z. B. `penny`). Alles, was für **jede** Website gleich ist,
steht einmal in `BaseUiTest`.

## So funktioniert ein Selenium-Test

```
JUnit-Test  ──▶  Selenium WebDriver  ──▶  ChromeDriver  ──▶  Google Chrome  ──▶  www.penny.at
```

| Schritt | Selenium direkt | in unseren Tests (`BaseUiTest`) |
| --- | --- | --- |
| Browser starten | `driver = new ChromeDriver(options);` | automatisch vor jedem Test (`@BeforeEach`) |
| Seite öffnen | `driver.get("https://www.penny.at/impressum");` | `open("impressum");` |
| Element finden | `driver.findElement(By.cssSelector("h1"))` | `waitForVisible(By.cssSelector("h1"))` |
| Klicken | `element.click();` | `clickElement(By.cssSelector("…"))` |
| Text lesen | `element.getText()` | `getText(By.cssSelector("…"))` |
| Warten | `wait.until(ExpectedConditions.urlToBe(…));` | `waitForUrl("impressum");` |
| Prüfen | `assertThat(driver.getTitle()).contains("PENNY");` | gleich (AssertJ) |
| Browser schließen | `driver.quit();` | automatisch nach jedem Test (`@AfterEach`) – auch wenn er fehlschlägt |

## Die Basisklasse `BaseUiTest`

Eine neue Testklasse erbt von `BaseUiTest` und gibt im Konstruktor nur ihre Website an. Alles andere
(Browser, Warten, Klicken) ist schon da:

```java
class MeineWebsiteTest extends BaseUiTest {

    MeineWebsiteTest() {
        super("https://www.example.com/");
    }

    @Test
    void startseiteHatTitel() {
        open();                                            // öffnet https://www.example.com/
    }
}
```

| Methode | Was sie tut |
| --- | --- |
| `open()` / `open("pfad")` | Startseite bzw. Unterseite der Website öffnen |
| `waitForVisible(locator)` | wartet (max. 10 s), bis ein passendes Element **sichtbar** ist, und gibt es zurück. Passen mehrere, wird das erste sichtbare genommen. |
| `clickElement(locator)` | wartet, bis das Element **sichtbar** ist → scrollt hin → wartet, bis es **klickbar** ist → klickt. Liegt kurz etwas davor (Banner, Animation), versucht es den Klick erneut. |
| `getText(locator)` | wartet, bis das Element sichtbar ist, und liefert seinen Text |
| `waitForInvisible(locator)` | wartet, bis ein Element verschwunden ist (z. B. ein Dialog) |
| `waitForUrl("pfad")` | wartet, bis die Adresse `https://…/pfad` ist |
| `waitForTitleContains("text")` | wartet, bis der Seitentitel den Text enthält |
| `shadowRoot(host)` | Zugriff auf ein Shadow DOM, z. B. `clickElement(shadowRoot(host), By.id("accept"))` |
| `chromeOptions()` | Browser-Einstellungen; kann eine Testklasse überschreiben (z. B. Fenstergröße für Mobil) |

**Warum immer warten?** Eine Website lädt Inhalte nach, blendet Banner ein und animiert Menüs. Klickt der
Test zu früh, findet er das Element nicht oder trifft etwas anderes. `clickElement` & Co. warten deshalb
immer **gezielt auf eine Bedingung** (sichtbar, klickbar, URL, Titel) – und zwar nur so lange wie nötig.
Ein festes `Thread.sleep(…)` ist entweder zu kurz (Test wackelt) oder zu lang (Test ist langsam).

## Die Beispiele (penny.at)

| Test | Was passiert |
| --- | --- |
| `openHomepageAndAcceptCookies` | Startseite öffnen, im Cookie-Banner „Alles erlauben“ klicken, prüfen, dass der Banner weg ist |
| `navigateToImpressumViaFooter` | wie oben, dann im Footer auf „Impressum“ klicken und prüfen, dass die URL `/impressum` ist |

Zwei Stolpersteine auf penny.at, die schon gelöst sind:

1. **Der Cookie-Banner liegt in einem Shadow DOM.** `driver.findElement(By.id("accept"))` findet den Button
   nicht. `acceptAllCookies()` in der Testklasse holt deshalb erst mit `shadowRoot(...)` den Inhalt von
   `aside#usercentrics-cmp-ui` und klickt dort.
2. **Den Impressum-Link gibt es zweimal** (für Desktop und Mobil). Nur einer ist sichtbar – `clickElement`
   nimmt automatisch den sichtbaren und scrollt zu ihm.

## Elemente selbst finden

In Chrome: Rechtsklick auf das Element → **Untersuchen** (DevTools). Dort sehen Sie das HTML.
Mit `Strg+F` (Mac: `Cmd+F`) im Elements-Tab können Sie einen CSS-Selektor direkt ausprobieren,
z. B. `footer a[href='/impressum']`.

## Übungen

Vorgehen: **Erst im Browser nachsehen, dann den Test schreiben.** Öffnen Sie die Seite in Chrome und
schauen Sie sich mit den DevTools an, was Sie prüfen wollen.

### Übung U1: Der Seitentitel des Impressums – `impressumPageHasTitleImpressum`

Die Fachabteilung sagt: „Die Impressum-Seite heißt **Impressum**.“ Prüfen Sie das mit einem UI-Test.

| Teil | Aufgabe |
| --- | --- |
| 1a | Den Test umsetzen (siehe `TODO`s) – mit den Methoden aus `BaseUiTest`: Startseite öffnen, Cookies akzeptieren, über den Footer zum Impressum, dann `assertThat(driver.getTitle()).isEqualTo("Impressum");` |
| 1b | Test ausführen. Ist er grün? Wenn nicht: Was ist der **tatsächliche** Titel? Lesen Sie die Fehlermeldung von AssertJ genau. |
| 1c | Wo steht der Seitentitel? Suchen Sie in den DevTools nach `<title>` und vergleichen Sie ihn mit dem Text im Browser-Tab. |
| 1d | Entscheiden Sie: Ist das ein **Fehler der Website** oder eine **ungenaue Anforderung**? Wie würden Sie die Prüfung formulieren (`isEqualTo`, `startsWith`, `contains`)? Was sind Vor- und Nachteile? |
| 1e | Die Seite lädt neue Inhalte nach, ohne komplett neu zu laden. Direkt nach dem Klick kann `getTitle()` deshalb noch den Titel der **Startseite** liefern. Sorgen Sie dafür, dass der Test **wartet**, bis der neue Titel da ist (Tipp: `waitForTitleContains(...)` in `BaseUiTest`). Warum ist `Thread.sleep(5000)` keine gute Lösung? |

Erledigt, wenn: der Test zuverlässig grün ist (mehrmals hintereinander ausführen!) und Sie erklären
können, warum der erste Versuch fehlgeschlagen ist.

### Vertiefung (optional)

#### Übung U2: Cookies ablehnen

Schreiben Sie einen Test, der im Cookie-Banner **„Alles ablehnen“** klickt (Tipp: DevTools → im Shadow DOM
von `aside#usercentrics-cmp-ui` nach dem Button suchen). Danach soll der Banner verschwunden sein.

#### Übung U3: Inhalt des Impressums prüfen

Prüfen Sie auf der Impressum-Seite den **sichtbaren Inhalt** statt nur den Titel, z. B. dass der Firmenname
oder die Firmenbuchnummer auf der Seite steht. Welche Prüfung ist **stabiler**: Titel, URL oder Seiteninhalt?
Welche sagt am meisten darüber aus, ob der Benutzer die richtige Seite sieht?

#### Übung U4: Weitere Footer-Links

Testen Sie weitere Links im Footer (z. B. Datenschutz, Kontakt) – am besten als `@ParameterizedTest`
mit `@CsvSource({"Linktext, erwartete URL", …})`.

## Lizenz

Copyright (c) 2026 Nagarro GmbH, Autor: Florin Balint. Lizenziert unter der
[Apache License 2.0](../LICENSE); Details zu Drittanbieter-Code siehe [NOTICE](../NOTICE).
