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

package com.example.taf.core;

import com.example.taf.config.TafConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

/**
 * Layer 1 – <b>core</b>: one browser session and the generic Selenium actions, all with built-in waiting.
 *
 * <p>This class knows nothing about a specific website. Page objects (layer 2) use it to interact with
 * their elements. The matching ChromeDriver is downloaded automatically by <b>Selenium Manager</b>.
 */
public class UiSession {

    private final Duration timeout = Duration.ofSeconds(TafConfig.getInt("browser.timeout.seconds"));

    private WebDriver driver;

    // =====================================================================
    // Browser lifecycle
    // =====================================================================

    /** Starts a fresh Chrome with the settings from {@code taf.properties}. */
    public void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=" + TafConfig.getInt("browser.window.width")
                + "," + TafConfig.getInt("browser.window.height"));
        if (TafConfig.isHeadless()) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
    }

    /** Closes the browser (safe to call even if it was never started). */
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("The browser is not started – call startBrowser() first");
        }
        return driver;
    }

    // =====================================================================
    // Navigation
    // =====================================================================

    /** Opens the given address. */
    public void open(String url) {
        getDriver().get(url);
    }

    // =====================================================================
    // Waiting
    // =====================================================================

    /**
     * Waits until {@code condition} returns something other than {@code null}/{@code false} and returns it.
     * Elements that are re-rendered in the meantime ({@link StaleElementReferenceException}) are tolerated.
     */
    public <T> T waitUntil(Function<WebDriver, T> condition) {
        return new WebDriverWait(getDriver(), timeout)
                .ignoring(StaleElementReferenceException.class)
                .until(condition);
    }

    /**
     * Waits until an element matching the locator is <b>visible</b> and returns it. If several elements
     * match, the first visible one is taken.
     */
    public WebElement waitForVisible(By locator) {
        try {
            return waitUntil(d -> d.findElements(locator).stream()
                    .filter(WebElement::isDisplayed)
                    .findFirst()
                    .orElse(null));
        } catch (TimeoutException e) {
            throw new TimeoutException("No visible element found for " + locator
                    + " (waited " + timeout.toSeconds() + " s)", e);
        }
    }

    /** Waits until no visible element matches the locator any more (e.g. a dialog was closed). */
    public void waitForInvisible(By locator) {
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // =====================================================================
    // Actions
    // =====================================================================

    /**
     * Clicks an element robustly: waits until it is <b>visible</b>, scrolls it into view, waits until it
     * is <b>clickable</b>, clicks – and retries while another element (banner, animation) is in front of it.
     */
    public void clickElement(By locator) {
        WebElement element = waitForVisible(locator);
        scrollIntoView(element);
        waitUntil(ExpectedConditions.elementToBeClickable(element));
        new WebDriverWait(getDriver(), timeout)
                .ignoring(ElementClickInterceptedException.class)
                .until(d -> {
                    element.click();
                    return true;
                });
    }

    /** Waits until the input field is visible, clears it and types the text. */
    public void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        scrollIntoView(element);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * The current <b>value</b> of an input field or text area. ({@code getText()} is not reliable for
     * form fields – what the user typed or the page filled in is in the {@code value} property.)
     */
    public String getValue(By locator) {
        return waitForVisible(locator).getDomProperty("value");
    }

    /** Waits until the element is visible and returns its visible text (trimmed). */
    public String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    /** Sets the value of an input field directly (e.g. to clear a read-only result field). */
    public void setValue(By locator, String value) {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].value = arguments[1];",
                waitForVisible(locator), value);
    }

    /** Scrolls the element to the middle of the window. */
    public void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }
}
