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

package com.example.ui.components;

import com.example.ui.base.UiSession;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Reusable component: the <b>Usercentrics</b> cookie banner (used by penny.at and many other websites).
 *
 * <p>The banner lives in a <b>shadow DOM</b> below {@code aside#usercentrics-cmp-ui}. A normal
 * {@code driver.findElement(...)} cannot see the buttons inside – this class searches in the shadow root.
 *
 * <p>Usage (JUnit or Cucumber):
 * <pre>{@code
 * UsercentricsCookieBanner cookieBanner = new UsercentricsCookieBanner(session);
 * cookieBanner.waitUntilLoaded();
 * cookieBanner.acceptAll();
 * }</pre>
 */
public class UsercentricsCookieBanner {

    private static final By HOST = By.cssSelector("aside#usercentrics-cmp-ui");
    private static final By ACCEPT_ALL_BUTTON = By.cssSelector("button#accept");

    private final UiSession session;

    public UsercentricsCookieBanner(UiSession session) {
        this.session = session;
    }

    /** Waits until the banner and its "accept all" button are visible, and returns that button. */
    public WebElement waitUntilLoaded() {
        return session.waitForVisible(session.shadowRoot(HOST), ACCEPT_ALL_BUTTON);
    }

    /** Clicks "Alles erlauben" (accept all) and waits until the banner is closed. */
    public void acceptAll() {
        session.clickElement(session.shadowRoot(HOST), ACCEPT_ALL_BUTTON);
        waitUntilClosed();
    }

    /** {@code true} as long as the "accept all" button is still present. */
    public boolean isOpen() {
        return !session.shadowRoot(HOST).findElements(ACCEPT_ALL_BUTTON).isEmpty();
    }

    private void waitUntilClosed() {
        session.newWait().until(d -> !isOpen());
    }
}
