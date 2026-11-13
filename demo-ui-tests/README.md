# demo-ui-tests

Course project for the **UI tests** part of the course "Einführung in die Testautomatisierung"
(introduction to test automation).

Here a test controls a **real browser** (Google Chrome) – just like a person would use the website:
open a page, close the cookie banner, click links, read text. The public website <https://www.penny.at/>
serves as the example. You will learn:

- how to remote-control a browser with **Selenium WebDriver**,
- how to find elements (**locators**: CSS selector, ID, …) and interact with them,
- why UI tests have to **wait** – and how to do it right (`WebDriverWait`),
- how to describe UI tests as readable scenarios with **Cucumber (BDD)**.

## Prerequisites

| Tool | Version |
| --- | --- |
| JDK | 17 or newer |
| Maven | 3.9 or newer (already included in IntelliJ) |
| Google Chrome | current version, installed normally |
| Selenium | 4.52.0 – downloaded automatically by Maven |
| JUnit | 6.1.3 |
| Cucumber | 8.0.4 (BDD tests with Gherkin) – downloaded automatically by Maven |
| AssertJ | 3.27.7 (readable assertions, e.g. `assertThat(title).startsWith("…")`) |
| Internet | yes – the tests open www.penny.at |

**You do not need to install ChromeDriver.** Selenium comes with **Selenium Manager**: on the first test
run it detects your Chrome version, downloads the matching ChromeDriver and stores it in
`~/.cache/selenium`. The first start therefore takes a few seconds longer.

## Running the tests

**In IntelliJ:**
- JUnit tests: open `src/test/java/com/example/ui/penny/PennyImpressumTest.java` and click the green ▶
  next to the class or a test.
- Cucumber scenarios: right-click `src/test/java/com/example/ui/cucumber/RunCucumberTest.java` →
  "Run 'RunCucumberTest'".

A Chrome window opens and you can watch what the test does.

**In the terminal:**

```bash
cd demo-ui-tests
mvn clean test                    # with a visible browser window
mvn clean test -Dheadless=true    # without a window (faster, e.g. on a CI server)
```

After the run, the Cucumber report is in `target/cucumber-report.html` (open it in the browser).
For a failed scenario it contains a screenshot of the page.

> **At the start some tests are red – that is intended.** They are the prepared exercises:
> `impressumPageHasTitleImpressum` (exercise 2) ends with `fail(...)`, and the second Cucumber scenario
> fails until the step from exercise 3 is completed.

## Project structure

```
demo-ui-tests
└── src/test
    ├── java/com/example/ui
    │   ├── base
    │   │   ├── UiSession.java                  browser session + reusable actions (clickElement, waiting, …)
    │   │   └── BaseUiTest.java                 base class for JUnit tests (starts/closes the browser)
    │   ├── components
    │   │   └── UsercentricsCookieBanner.java   reusable component: the cookie banner
    │   ├── penny
    │   │   ├── PennyImpressumTest.java         JUnit: example 1, example 2 and exercise 2
    │   │   └── PennyWebsiteTest.java           JUnit: start page and imprint, exercise 1
    │   └── cucumber
    │       ├── RunCucumberTest.java            runs all .feature files (JUnit Platform Suite)
    │       ├── BrowserHooks.java               starts/closes the browser for every scenario
    │       └── steps
    │           ├── WebsiteSteps.java           general steps: choose website, open page, check title
    │           └── CookieSteps.java            steps for the cookie prompt
    └── resources/features/penny
        └── startpage.feature                   BDD scenarios for the PENNY start page
```

Each website gets its own package (e.g. `penny`). Everything that is the same for **every** website lives
once in `UiSession`. Both kinds of tests – JUnit and Cucumber – use the same `UiSession` and the same
components.

## How a Selenium test works

```
JUnit test / Cucumber step  ──▶  Selenium WebDriver  ──▶  ChromeDriver  ──▶  Google Chrome  ──▶  www.penny.at
```

| Step | Plain Selenium | In our tests (`UiSession`) |
| --- | --- | --- |
| Start the browser | `driver = new ChromeDriver(options);` | automatically before every test / scenario |
| Open a page | `driver.get("https://www.penny.at/impressum");` | `open("impressum");` |
| Find an element | `driver.findElement(By.cssSelector("h1"))` | `waitForVisible(By.cssSelector("h1"))` |
| Click | `element.click();` | `clickElement(By.cssSelector("…"))` |
| Read text | `element.getText()` | `getText(By.cssSelector("…"))` |
| Wait | `wait.until(ExpectedConditions.urlToBe(…));` | `waitForUrl("impressum");` |
| Assert | `assertThat(driver.getTitle()).contains("PENNY");` | the same (AssertJ) |
| Close the browser | `driver.quit();` | automatically after every test / scenario – even if it fails |

## The base class `BaseUiTest`

A new JUnit test class extends `BaseUiTest` and only names its website in the constructor. Everything
else (browser, waiting, clicking) is already there:

```java
class MyWebsiteTest extends BaseUiTest {

    MyWebsiteTest() {
        super("https://www.example.com/");
    }

    @Test
    void startPageHasTitle() {
        open();                                            // opens https://www.example.com/
        assertThat(driver.getTitle()).isEqualTo("Example Domain");
    }
}
```

The actions come from `UiSession`:

| Method | What it does |
| --- | --- |
| `open()` / `open("path")` | open the start page or a sub page of the website |
| `waitForVisible(locator)` | waits (max. 10 s) until a matching element is **visible** and returns it. If several match, the first visible one is taken. |
| `clickElement(locator)` | waits until the element is **visible** → scrolls to it → waits until it is **clickable** → clicks. If something is briefly in front of it (banner, animation), the click is retried. |
| `getText(locator)` | waits until the element is visible and returns its text |
| `waitForInvisible(locator)` | waits until an element has disappeared (e.g. a dialog) |
| `waitForUrl("path")` | waits until the address is `https://…/path` |
| `waitForTitle()` | waits until the page has a (non-empty) title and returns it |
| `waitForTitleContains("text")` | waits until the page title contains the text |
| `shadowRoot(host)` | access to a shadow DOM, e.g. `clickElement(shadowRoot(host), By.id("accept"))` |
| `chromeOptions()` | browser settings; a test class can override it (e.g. window size for mobile) |

**Why always wait?** A website loads content later, fades in banners and animates menus. If the test
clicks too early, it does not find the element or hits something else. `clickElement` & co. therefore
always wait **for a specific condition** (visible, clickable, URL, title) – and only as long as necessary.
A fixed `Thread.sleep(…)` is either too short (flaky test) or too long (slow test).

## The examples (penny.at)

| Test | What happens |
| --- | --- |
| `openHomepageAndAcceptCookies` | open the start page, click "Alles erlauben" (accept all) in the cookie banner, check that the banner is gone |
| `navigateToImpressumViaFooter` | as above, then click "Impressum" (imprint) in the footer and check that the URL is `/impressum` |

Two pitfalls on penny.at that are already solved:

1. **The cookie banner lives in a shadow DOM.** `driver.findElement(By.id("accept"))` does not find the
   button. The component `UsercentricsCookieBanner` therefore first gets the content of
   `aside#usercentrics-cmp-ui` with `shadowRoot(...)` and clicks there.
2. **The imprint link exists twice** (for desktop and mobile). Only one is visible – `clickElement`
   automatically takes the visible one and scrolls to it.

## BDD with Cucumber

With **Behaviour-Driven Development (BDD)** the expected behaviour is written down as scenarios in
plain language (**Gherkin**) – readable for testers, developers and the business department alike.
Cucumber connects every sentence with a Java method (**step definition**).

```gherkin
Feature: PENNY start page

  Scenario: The start page loads with cookie prompt and title
    Given I want to verify the PENNY website
    When I call the startpage
    Then the cookie prompt is loaded
    And the page has a title
```

| Keyword | Meaning | Test pattern |
| --- | --- | --- |
| `Given` | starting situation | Arrange |
| `When` | action | Act |
| `Then` | expected result | Assert |
| `And` | continues the previous keyword | |

**How a sentence finds its Java method:**

```java
@Given("I want to verify the {word} website")     // {word} → "PENNY" is passed as parameter
public void iWantToVerifyTheWebsite(String name) { ... }
```

The steps are split by topic, so they can be reused in any feature file:

| Class | Steps |
| --- | --- |
| `WebsiteSteps` | `Given I want to verify the {word} website`, `When I call the startpage`, `When I call the page "{path}"`, `Then the page has a title`, `Then the page title contains "{text}"` |
| `CookieSteps` | `Then the cookie prompt is loaded`, `When I accept all cookies`, `Then the cookie prompt is closed` |

**How do the step classes share one browser?** Through **PicoContainer** (`cucumber-picocontainer`):
for every scenario Cucumber creates exactly one `UiSession` and passes it into the constructor of every
step class and of `BrowserHooks`. `BrowserHooks` starts the browser before each scenario (`@Before`)
and closes it afterwards (`@After`) – after a failed scenario it first attaches a screenshot to the report.

**New website?** Add it to the map `WEBSITES` in `WebsiteSteps`, then you can write
`Given I want to verify the BILLA website`.

## Finding elements yourself

In Chrome: right-click the element → **Inspect** (DevTools). There you see the HTML.
With `Ctrl+F` (Mac: `Cmd+F`) in the Elements tab you can try out a CSS selector directly,
e.g. `footer a[href='/impressum']`.

## Exercises

Approach: **look in the browser first, then write the test.** Open the page in Chrome and use the
DevTools to look at what you want to check.

| No. | Exercise | Where |
| --- | --- | --- |
| 1 | Check the title of the start page | `PennyWebsiteTest` (JUnit) |
| 2 | Check the title of the imprint page | `PennyImpressumTest` (JUnit) |
| 3 | Complete a Cucumber step | `WebsiteSteps` (Cucumber) |
| 4 | Your first own BDD scenario | new `.feature` file + `FooterSteps` (Cucumber) |
| 5 | Scenario Outline | new `.feature` file (Cucumber) |
| 6 | Reject cookies (optional) | `UsercentricsCookieBanner` |
| 7 | Check the content of the imprint (optional) | JUnit or Cucumber |
| 8 | More footer links (optional) | JUnit or Cucumber |

### Exercise 1: The title of the start page – `PennyWebsiteTest`

The test `openHomepageAndAcceptCookies` in `PennyWebsiteTest` opens the start page and accepts the
cookies – but it does not check anything yet (see the `TODO`).

| Part | Task |
| --- | --- |
| 1.1 | Run the test as it is. Is it green? Why can a test **without an assertion** never fail? |
| 1.2 | Add the assertion: the page title is `PENNY Österreich \| Da schau her.` (`assertThat(driver.getTitle()).isEqualTo(...)`). |
| 1.3 | Change the expected title on purpose (e.g. remove the dot at the end) and run the test again. Read the failure message, then undo the change. |

Done when: the test is green and fails as soon as the expected title is wrong.

### Exercise 2: The title of the imprint page – `impressumPageHasTitleImpressum`

The business department says: "The imprint page is called **Impressum**." Check this with a UI test
in `PennyImpressumTest`.

| Part | Task |
| --- | --- |
| 2.1 | Implement the test (see the `TODO`s) – using the methods from `BaseUiTest`: open the start page, accept the cookies, go to the imprint via the footer, then `assertThat(driver.getTitle()).isEqualTo("Impressum");` |
| 2.2 | Run the test. Is it green? If not: what is the **actual** title? Read AssertJ's failure message carefully. |
| 2.3 | Where is the page title defined? Search for `<title>` in the DevTools and compare it with the text in the browser tab. |
| 2.4 | Decide: is this a **bug in the website** or an **imprecise requirement**? How would you phrase the check (`isEqualTo`, `startsWith`, `contains`)? What are the pros and cons? |
| 2.5 | The page loads new content without a full reload. Right after the click, `getTitle()` may therefore still return the title of the **start page**. Make sure the test **waits** until the new title is there (hint: `waitForTitleContains(...)`). Why is `Thread.sleep(5000)` not a good solution? |

Done when: the test is reliably green (run it several times in a row!) and you can explain why the
first attempt failed.

### Exercise 3: Complete a Cucumber step – `WebsiteSteps`

The second scenario in `startpage.feature` ("Accepting all cookies closes the cookie prompt") ends with
`And the page title contains "PENNY"`. The Java method behind this sentence is not finished yet.

| Part | Task |
| --- | --- |
| 3.1 | Run `RunCucumberTest`. Which scenario is red, and at which step? What does the failure message say? |
| 3.2 | Find the method for the sentence `the page title contains {string}` in `WebsiteSteps`. How does the text `"PENNY"` from the feature file get into the parameter `text`? |
| 3.3 | Complete the `TODO`: read the page title from the browser (hint: `session.getDriver()`) and check that it contains the expected text. |

Done when: both scenarios in `startpage.feature` are green.

### Exercise 4: Your first own BDD scenario – the imprint

Write exercise 2 as a Cucumber scenario. Create `src/test/resources/features/penny/impressum.feature`:

```gherkin
Feature: PENNY imprint

  Scenario: The imprint can be reached via the footer
    Given I want to verify the PENNY website
    When I call the startpage
    And I accept all cookies
    And I navigate to the imprint via the footer
    Then the page title contains "Impressum"
```

| Part | Task |
| --- | --- |
| 4.1 | Run `RunCucumberTest`. Which step is **undefined**? Cucumber prints a code snippet for it – where? |
| 4.2 | Implement the missing step in a new class `FooterSteps` (package `com.example.ui.cucumber.steps`). It gets the `UiSession` in the constructor like the other step classes. Use `clickElement(...)`. |
| 4.3 | Which steps could you reuse without writing any code? |

Done when: both feature files are green and you can explain how Cucumber finds the Java method for a sentence.

### Exercise 5: Scenario Outline – several pages in one scenario

Check with **one** `Scenario Outline` and an `Examples:` table that several pages of penny.at have the
expected title, e.g. the start page and `impressum`. Use the existing step `When I call the page "{path}"`.

### In depth (optional)

#### Exercise 6: Reject cookies

Write a test that clicks **"Alles ablehnen"** (deny all) in the cookie banner (hint: DevTools → search
for the button in the shadow DOM of `aside#usercentrics-cmp-ui`). Afterwards the banner should be gone.
Add the method to `UsercentricsCookieBanner` so that JUnit tests and Cucumber steps can both use it.

#### Exercise 7: Check the content of the imprint

On the imprint page check the **visible content** instead of only the title, e.g. that the company name
or the company register number is on the page. Which check is **more stable**: title, URL or page
content? Which one tells you most about whether the user sees the right page?

#### Exercise 8: More footer links

Test more links in the footer (e.g. privacy policy, contact) – ideally as a `@ParameterizedTest`
with `@CsvSource({"link text, expected URL", …})`, or as a Cucumber `Scenario Outline`.
Which of the two variants is easier to read for the business department?

## License

Copyright (c) 2026 Nagarro GmbH, author: Florin Balint. Licensed under the
[Apache License 2.0](../LICENSE); for third-party code see [NOTICE](../NOTICE).
