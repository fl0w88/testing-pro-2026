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
 * UI-Tests für <a href="https://www.penny.at/">www.penny.at</a>.
 *
 * <p>Browser starten/schließen, Klicken und Warten erledigt {@link BaseUiTest}. Diese Klasse gibt nur
 * die Website an (Konstruktor) und enthält, was speziell für Penny ist: die Locator und die Testfälle.
 */
class PennyWebsiteTest extends BaseUiTest {

    /** Der Cookie-Banner (Usercentrics) liegt in einem Shadow DOM unter diesem Element. */
    private static final By COOKIE_BANNER_HOST = By.cssSelector("aside#usercentrics-cmp-ui");
    private static final By ACCEPT_ALL_COOKIES = By.cssSelector("button#accept");

    /** Den Impressum-Link gibt es im Footer zweimal (Desktop/Mobil) – clickElement nimmt den sichtbaren. */
    private static final By FOOTER_IMPRESSUM_LINK = By.cssSelector("footer a[href='/impressum']");

    PennyWebsiteTest() {
        super("https://www.penny.at/");
    }

    // =====================================================================
    // Beispiel 1: Startseite öffnen und Cookies akzeptieren
    // =====================================================================

    @Test
    @DisplayName("Startseite öffnen und alle Cookies akzeptieren")
    void openHomepageAndAcceptCookies() {
        // Arrange + Act
        open();
        acceptAllCookies();

        // Assert
        // TODO: Hier prüfen ob der Titel der Seite "PENNY Österreich | Da schau her." ist.
    }

    // =====================================================================
    // Beispiel 2: Über den Footer zum Impressum navigieren
    // =====================================================================

    @Test
    @DisplayName("Über den Link im Footer zum Impressum navigieren")
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
    // Penny-spezifische Hilfsmethoden
    // =====================================================================

    /**
     * Klickt im Cookie-Banner auf "Alles erlauben" und wartet, bis der Button verschwunden ist.
     * Der Banner steckt in einem Shadow DOM – deshalb wird im {@code shadowRoot(...)} gesucht.
     */
    private void acceptAllCookies() {
        clickElement(shadowRoot(COOKIE_BANNER_HOST), ACCEPT_ALL_COOKIES);
        wait.until(d -> shadowRoot(COOKIE_BANNER_HOST).findElements(ACCEPT_ALL_COOKIES).isEmpty());
    }
}
