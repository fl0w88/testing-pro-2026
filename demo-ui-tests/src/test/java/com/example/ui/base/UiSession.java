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
 * One browser session against one website: starts/stops Chrome and offers reusable actions
 * ({@link #open(String)}, {@link #clickElement(By)}, {@link #waitForVisible(By)}, {@link #getText(By)}, ...),
 * all of them with built-in waiting.
 *
 * <p>It is used in two ways:
 * <ul>
 *   <li><b>JUnit tests</b> extend {@link BaseUiTest}, which extends this class and starts/stops the browser
 *       around every test.</li>
 *   <li><b>Cucumber steps</b> get one instance per scenario injected (PicoContainer), see
 *       {@code com.example.ui.cucumber.BrowserHooks}.</li>
 * </ul>
 *
 * <p>The matching ChromeDriver is downloaded automatically by <b>Selenium Manager</b> (built into Selenium).
 * Run without a visible browser window: {@code mvn test -Dheadless=true}
 */
public class UiSession {

    /** The helper methods wait at most this long for a condition. */
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    /** The website under test, e.g. {@code https://www.penny.at/}. */
    protected String baseUrl;

    protected WebDriver driver;
    protected WebDriverWait wait;

    /** The website can be set later with {@link #setBaseUrl(String)} (e.g. by a Cucumber "Given" step). */
    public UiSession() {
    }

    /**
     * @param baseUrl start address of the website, e.g. {@code https://www.penny.at/}
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public WebDriver getDriver() {
        return driver;
    }

    // =====================================================================
    // Browser lifecycle
    // =====================================================================

    /** Starts a fresh Chrome. */
    public void startBrowser() {
        driver = new ChromeDriver(chromeOptions());
        wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    /** Closes the browser (safe to call even if it was never started). */
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    /**
     * Browser settings. A subclass can override this method to add options
     * (e.g. a smaller window size for mobile tests).
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
     * A separate wait object for special cases, e.g. {@code newWait().ignoring(...)}.
     * ({@code ignoring} modifies the wait object – that is why the shared {@link #wait} is not used.)
     */
    public WebDriverWait newWait() {
        return new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    // =====================================================================
    // Navigation
    // =====================================================================

    /** Opens the start page of the website. */
    public void open() {
        open("");
    }

    /** Opens a page relative to the website, e.g. {@code open("impressum")} → {@code https://www.penny.at/impressum}. */
    public void open(String path) {
        driver.get(url(path));
    }

    /** Builds a full address: {@code url("impressum")} → {@code https://www.penny.at/impressum}. */
    public String url(String path) {
        if (baseUrl == null) {
            throw new IllegalStateException("No website set – call setBaseUrl(...) first");
        }
        return baseUrl + (path.startsWith("/") ? path.substring(1) : path);
    }

    // =====================================================================
    // Finding elements – always with waiting
    // =====================================================================

    /**
     * Waits until an element matching the locator is <b>visible</b> and returns it.
     * <p>
     * If several elements match (e.g. the same link for desktop and mobile), the first
     * <b>visible</b> one is taken – invisible ones are skipped.
     *
     * @throws TimeoutException if no visible element appears within {@link #DEFAULT_TIMEOUT}
     */
    public WebElement waitForVisible(By locator) {
        return waitForVisible(driver, locator);
    }

    /** Like {@link #waitForVisible(By)}, but only searches inside {@code context} (an element or a shadow root). */
    public WebElement waitForVisible(SearchContext context, By locator) {
        try {
            return newWait().ignoring(StaleElementReferenceException.class)
                    .until(d -> context.findElements(locator).stream()
                            .filter(WebElement::isDisplayed)
                            .findFirst()
                            .orElse(null));
        } catch (TimeoutException e) {
            throw new TimeoutException("No visible element found for " + locator
                    + " (waited " + DEFAULT_TIMEOUT.toSeconds() + " s)", e);
        }
    }

    /** Waits until no visible element matches the locator any more (e.g. a dialog was closed). */
    public void waitForInvisible(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // =====================================================================
    // Actions
    // =====================================================================

    /**
     * Clicks an element – robustly:
     * <ol>
     *   <li>waits until the element is <b>visible</b>,</li>
     *   <li>scrolls it to the middle of the window,</li>
     *   <li>waits until it is <b>clickable</b> (visible and enabled),</li>
     *   <li>clicks – and retries as long as another element (banner, animation) is in front of it.</li>
     * </ol>
     */
    public void clickElement(By locator) {
        clickElement(waitForVisible(locator));
    }

    /** Like {@link #clickElement(By)}, but only searches inside {@code context} (e.g. a shadow root). */
    public void clickElement(SearchContext context, By locator) {
        clickElement(waitForVisible(context, locator));
    }

    /** Like {@link #clickElement(By)} for an element that was already found. */
    public void clickElement(WebElement element) {
        scrollIntoView(element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        newWait().ignoring(ElementClickInterceptedException.class)
                .until(d -> {
                    element.click();
                    return true;
                });
    }

    /** Waits until the element is visible and returns its visible text (trimmed). */
    public String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    /** Scrolls the element to the middle of the window (e.g. links at the very bottom of the footer). */
    public void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    // =====================================================================
    // Shadow DOM
    // =====================================================================

    /**
     * Waits until the host element exists and returns its <b>shadow root</b>.
     * <p>
     * Some components (e.g. cookie banners) encapsulate their HTML in a shadow DOM. A normal
     * {@code driver.findElement(...)} cannot see inside – search in the shadow root instead:
     * {@code clickElement(shadowRoot(host), By.cssSelector("button#accept"))}.
     */
    public SearchContext shadowRoot(By host) {
        return wait.until(d -> d.findElement(host).getShadowRoot());
    }

    // =====================================================================
    // Waiting for page changes
    // =====================================================================

    /** Waits until the current address is exactly {@code url(path)}. */
    public void waitForUrl(String path) {
        wait.until(ExpectedConditions.urlToBe(url(path)));
    }

    /** Waits until the page title (browser tab) contains the text. Useful when a page updates its title later. */
    public void waitForTitleContains(String text) {
        wait.until(ExpectedConditions.titleContains(text));
    }

    /** Waits until the page title (browser tab) is not empty and returns it. */
    public String waitForTitle() {
        return wait.until(d -> {
            String title = d.getTitle();
            return title == null || title.isBlank() ? null : title;
        });
    }
}
