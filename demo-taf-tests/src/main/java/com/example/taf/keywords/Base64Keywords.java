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

package com.example.taf.keywords;

import com.example.taf.core.UiSession;
import com.example.taf.pages.Base64DecodePage;
import com.example.taf.pages.Base64EncodePage;
import com.example.taf.pages.CookieConsentDialog;

/**
 * Layer 3 – <b>keywords</b>: business actions in the language of the tester ("open the site",
 * "encode this text") – no locators, no Selenium.
 *
 * <p>A keyword combines one or more page object calls. Test cases only call keywords, so they stay
 * short and readable, and they do not change when the website's HTML changes.
 */
public class Base64Keywords {

    private final Base64EncodePage encodePage;
    private final Base64DecodePage decodePage;
    private final CookieConsentDialog cookieConsentDialog;

    public Base64Keywords(UiSession session) {
        this.encodePage = new Base64EncodePage(session);
        this.decodePage = new Base64DecodePage(session);
        this.cookieConsentDialog = new CookieConsentDialog(session);
    }

    /** Opens base64encode.org. */
    public void openBase64Site() {
        encodePage.open();
    }

    /** Opens base64decode.org. */
    public void openBase64DecodeSite() {
        decodePage.open();
    }

    /** Closes the cookie dialog by agreeing. */
    public void closeCookies() {
        cookieConsentDialog.agree();
    }

    /** Encodes the text on base64encode.org (the site must be open) and returns the result. */
    public String encodeText(String text) {
        return encodePage.encode(text);
    }

    /** Decodes the Base64 text on base64decode.org (the site must be open) and returns the result. */
    public String decodeText(String base64) {
        return decodePage.decode(base64);
    }
}
