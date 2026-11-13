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

package com.example.taf.pages;

import com.example.taf.core.UiSession;
import org.openqa.selenium.By;

/**
 * Layer 2 – <b>page object</b> (component): the cookie consent dialog ("We value your privacy").
 * base64encode.org and base64decode.org both use the same dialog (Quantcast Choice).
 */
public class CookieConsentDialog {

    private static final By AGREE_BUTTON = By.id("accept-btn");

    private final UiSession session;

    public CookieConsentDialog(UiSession session) {
        this.session = session;
    }

    /** Clicks "AGREE" and waits until the dialog is closed. */
    public void agree() {
        session.clickElement(AGREE_BUTTON);
        session.waitForInvisible(AGREE_BUTTON);
    }
}
