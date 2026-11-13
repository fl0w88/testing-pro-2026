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
import com.example.ui.components.UsercentricsCookieBanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * JUnit UI tests for <a href="https://www.penny.at/">www.penny.at</a>.
 *
 * <p>Starting/closing the browser, clicking and waiting is done by {@link BaseUiTest}. This class only
 * names the website (constructor) and contains what is specific to Penny: locators and test cases.
 * The same start page is also tested with Cucumber (BDD), see {@code features/penny/startpage.feature}.
 */
class PennyImpressumTest extends BaseUiTest {

    /** The footer contains the imprint link twice (desktop/mobile) – clickElement takes the visible one. */
    private static final By FOOTER_IMPRESSUM_LINK = By.cssSelector("footer a[href='/impressum']");

    private final UsercentricsCookieBanner cookieBanner = new UsercentricsCookieBanner(this);

    PennyImpressumTest() {
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
        cookieBanner.acceptAll();

        // Assert
        assertThat(driver.getTitle()).contains("PENNY");
        assertThat(cookieBanner.isOpen())
                .as("The cookie banner is closed after clicking 'Alles erlauben'")
                .isFalse();
    }

    // =====================================================================
    // Example 2: navigate to the imprint via the footer
    // =====================================================================

    @Test
    @DisplayName("Navigate to the imprint (Impressum) via the footer link")
    void navigateToImpressumViaFooter() {
        // Arrange
        open();
        cookieBanner.acceptAll();

        // Act
        clickElement(FOOTER_IMPRESSUM_LINK);

        // Assert
        waitForUrl("impressum");
        assertThat(driver.getCurrentUrl()).isEqualTo("https://www.penny.at/impressum");
    }

    // =====================================================================
    // Exercise 2: check the title of the imprint page (see README)
    // =====================================================================

    @Test
    @DisplayName("Exercise 2: the imprint page is called 'Impressum'")
    void impressumPageHasTitleImpressum() {
        // TODO Arrange: open the start page and accept the cookies (as in example 2)
        // TODO Act:     navigate to the imprint via the footer
        // TODO Assert:  check that the page title (driver.getTitle()) is "Impressum"
        fail("Exercise 2: not implemented yet");
    }
}
