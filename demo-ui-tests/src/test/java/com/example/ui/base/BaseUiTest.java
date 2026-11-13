/*
 * Copyright (c) 2026 Nagarro GmbH. Author: Florin Balint.
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Demo code for training purposes. Distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for details.
 */

package com.example.ui.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Gemeinsame Basisklasse für alle UI-Tests.
 *
 * <p>Eine neue Testklasse muss nur von dieser Klasse erben und ihre Website (Domain) angeben:
 * <pre>{@code
 * class MeineWebsiteTest extends BaseUiTest {
 *
 *     MeineWebsiteTest() {
 *         super("https://www.example.com/");
 *     }
 *
 *     @Test
 *     void startseiteHatTitel() {
 *         open();
 *         assertThat(driver.getTitle()).contains("Example");
 *     }
 * }
 * }</pre>
 *
 * <p>Die Basisklasse übernimmt:
 * <ul>
 *   <li><b>Browser-Lebenszyklus:</b> vor jedem Test einen frischen Chrome starten, danach schließen.</li>
 *   <li><b>Wiederverwendbare Aktionen:</b> {@link #open(String)}, {@link #clickElement(By)},
 *       {@link #waitForVisible(By)}, {@link #getText(By)}, … – alle mit eingebautem Warten.</li>
 * </ul>
 *
 * <p>Den passenden ChromeDriver lädt der in Selenium eingebaute <b>Selenium Manager</b> automatisch.
 * Ohne sichtbares Browserfenster ausführen: {@code mvn test -Dheadless=true}
 */
public abstract class BaseUiTest {

    /** So lange warten die Hilfsmethoden höchstens, bis eine Bedingung erfüllt ist. */
    protected static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    /** Die Website, die diese Testklasse testet, z. B. {@code https://www.penny.at/}. */
    protected final String baseUrl;

    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * @param baseUrl die Start-Adresse der Website (mit abschließendem {@code /}), z. B. {@code https://www.penny.at/}
     */
    protected BaseUiTest(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }

    // =====================================================================
    // Browser-Lebenszyklus
    // =====================================================================

    @BeforeEach
    void startBrowser() {
        driver = new ChromeDriver(chromeOptions());
        wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Browser-Einstellungen. Eine Testklasse kann diese Methode überschreiben, um weitere Optionen
     * zu setzen (z. B. eine andere Fenstergröße für Mobil-Tests).
     */
    protected ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1400,1000", "--lang=de-AT");
        if (Boolean.getBoolean("headless")) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    /**
     * Ein eigenes Wait-Objekt für Sonderfälle, z. B. {@code newWait().ignoring(...)}.
     * ({@code ignoring} verändert das Wait-Objekt – deshalb nicht das gemeinsame {@link #wait} verwenden.)
     */
    protected WebDriverWait newWait() {
        return new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    // =====================================================================
    // Navigation
    // =====================================================================

    /** Öffnet die Startseite der Website. */
    protected void open() {
        open("");
    }

    /**
     * Öffnet eine Seite relativ zur Website, z. B. {@code open("impressum")} → {@code https://www.penny.at/impressum}.
     */
    protected void open(String path) {
        driver.get(url(path));
    }

    /** Baut eine vollständige Adresse: {@code url("impressum")} → {@code https://www.penny.at/impressum}. */
    protected String url(String path) {
        return baseUrl + (path.startsWith("/") ? path.substring(1) : path);
    }

    // =====================================================================
    // Elemente finden – immer mit Warten
    // =====================================================================

    /**
     * Wartet, bis ein Element, das zum Locator passt, <b>sichtbar</b> ist, und gibt es zurück.
     * <p>
     * Passen mehrere Elemente (z. B. derselbe Link für Desktop und Mobil), wird das erste
     * <b>sichtbare</b> genommen – unsichtbare werden übersprungen.
     *
     * @throws TimeoutException wenn nach {@link #DEFAULT_TIMEOUT} kein sichtbares Element da ist
     */
    protected WebElement waitForVisible(By locator) {
        return waitForVisible(driver, locator);
    }

    /** Wie {@link #waitForVisible(By)}, sucht aber nur innerhalb von {@code context} (Element oder Shadow Root). */
    protected WebElement waitForVisible(SearchContext context, By locator) {
        try {
            return newWait().ignoring(StaleElementReferenceException.class)
                    .until(d -> context.findElements(locator).stream()
                            .filter(WebElement::isDisplayed)
                            .findFirst()
                            .orElse(null));
        } catch (TimeoutException e) {
            throw new TimeoutException("Kein sichtbares Element gefunden für " + locator
                    + " (gewartet: " + DEFAULT_TIMEOUT.toSeconds() + " s)", e);
        }
    }

    /** Wartet, bis kein sichtbares Element mehr zum Locator passt (z. B. ein Dialog wurde geschlossen). */
    protected void waitForInvisible(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // =====================================================================
    // Aktionen
    // =====================================================================

    /**
     * Klickt auf ein Element – robust:
     * <ol>
     *   <li>wartet, bis das Element <b>sichtbar</b> ist,</li>
     *   <li>scrollt es in die Mitte des Fensters,</li>
     *   <li>wartet, bis es <b>klickbar</b> ist (sichtbar und aktiviert),</li>
     *   <li>klickt – und versucht es erneut, solange ein anderes Element (Banner, Animation) davor liegt.</li>
     * </ol>
     */
    protected void clickElement(By locator) {
        clickElement(waitForVisible(locator));
    }

    /** Wie {@link #clickElement(By)}, sucht das Element aber nur innerhalb von {@code context} (z. B. einem Shadow Root). */
    protected void clickElement(SearchContext context, By locator) {
        clickElement(waitForVisible(context, locator));
    }

    /** Wie {@link #clickElement(By)} für ein bereits gefundenes Element. */
    protected void clickElement(WebElement element) {
        scrollIntoView(element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        newWait().ignoring(ElementClickInterceptedException.class)
                .until(d -> {
                    element.click();
                    return true;
                });
    }

    /** Wartet, bis das Element sichtbar ist, und gibt seinen sichtbaren Text zurück (ohne Leerzeichen am Rand). */
    protected String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    /** Scrollt das Element in die Mitte des Fensters (z. B. Links ganz unten im Footer). */
    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    // =====================================================================
    // Shadow DOM
    // =====================================================================

    /**
     * Wartet, bis das Host-Element da ist, und gibt seinen <b>Shadow Root</b> zurück.
     * <p>
     * Manche Komponenten (z. B. Cookie-Banner) kapseln ihr HTML in einem Shadow DOM. Ein normales
     * {@code driver.findElement(...)} sieht dort nicht hinein – man sucht stattdessen im Shadow Root:
     * {@code clickElement(shadowRoot(host), By.cssSelector("button#accept"))}.
     */
    protected SearchContext shadowRoot(By host) {
        return wait.until(d -> d.findElement(host).getShadowRoot());
    }

    // =====================================================================
    // Warten auf Seitenwechsel
    // =====================================================================

    /** Wartet, bis die aktuelle Adresse genau {@code url(path)} ist. */
    protected void waitForUrl(String path) {
        wait.until(ExpectedConditions.urlToBe(url(path)));
    }

    /** Wartet, bis der Seitentitel (Browser-Tab) den Text enthält. Nützlich, wenn eine Seite den Titel nachlädt. */
    protected void waitForTitleContains(String text) {
        wait.until(ExpectedConditions.titleContains(text));
    }
}
