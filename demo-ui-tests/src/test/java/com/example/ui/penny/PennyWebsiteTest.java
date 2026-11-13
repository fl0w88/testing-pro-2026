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

package com.example.ui.penny;

import com.example.ui.base.BaseUiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * UI tests for <a href="https://www.penny.at/">www.penny.at</a>.
 *
 * <p>Starting/closing the browser, clicking and waiting is done by {@link BaseUiTest}. This class only
 * names the website (constructor) and contains what is specific to Penny: locators and test cases.
 */
class PennyWebsiteTest extends BaseUiTest {

    /** The cookie banner (Usercentrics) lives in a shadow DOM below this element. */
    private static final By COOKIE_BANNER_HOST = By.cssSelector("aside#usercentrics-cmp-ui");
    private static final By ACCEPT_ALL_COOKIES = By.cssSelector("button#accept");

    /** The footer contains the imprint link twice (desktop/mobile) – clickElement takes the visible one. */
    private static final By FOOTER_IMPRESSUM_LINK = By.cssSelector("footer a[href='/impressum']");

    PennyWebsiteTest() {
        super("https://www.penny.at/");
    }

    // =====================================================================
    // Example 1: open the start page and accept the cookies
    // =====================================================================

    @Test
    @DisplayName("Open the start page and accept all cookies")
    void openHomepageAndAcceptCookies() {
        // Arrange + Act
        open();
        acceptAllCookies();

        // Assert
        // TODO Exercise 1: check here that the page title is "PENNY Österreich | Da schau her." (see README)
    }

    // =====================================================================
    // Example 2: navigate to the imprint via the footer
    // =====================================================================

    @Test
    @DisplayName("Navigate to the imprint (Impressum) via the footer link")
    void navigateToImpressumViaFooter() {
        // Arrange
        open();
        acceptAllCookies();

        // Act
        clickElement(FOOTER_IMPRESSUM_LINK);

        // Assert
        waitForUrl("impressum");
        assertThat(driver.getCurrentUrl()).isEqualTo("https://www.penny.at/impressum");
    }


    // =====================================================================
    // Penny-specific helper methods
    // =====================================================================

    /**
     * Clicks "Alles erlauben" (accept all) in the cookie banner and waits until the button is gone.
     * The banner lives in a shadow DOM – that is why the search happens in {@code shadowRoot(...)}.
     */
    private void acceptAllCookies() {
        clickElement(shadowRoot(COOKIE_BANNER_HOST), ACCEPT_ALL_COOKIES);
        wait.until(d -> shadowRoot(COOKIE_BANNER_HOST).findElements(ACCEPT_ALL_COOKIES).isEmpty());
    }
}
