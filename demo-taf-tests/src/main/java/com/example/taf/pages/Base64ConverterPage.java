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
 * Layer 2 – <b>page object</b>: what the encode page and the decode page have in common.
 *
 * <p>Both pages have the same form: a text area {@code #input}, a text area {@code #output} with the
 * result and a "Live mode" switch. A page object hides these locators – if the website changes, only
 * this class has to be adapted, not the tests.
 *
 * <p><b>Why live mode instead of the ENCODE/DECODE button?</b> The button submits the form to the server,
 * and base64decode.org answers automated browsers with a bot check ("Just a moment..."). In live mode the
 * page converts while you type, without submitting the form – so the same code works on both sites.
 */
public abstract class Base64ConverterPage {

    private static final By INPUT = By.id("input");
    private static final By OUTPUT = By.id("output");
    private static final By LIVE_MODE_SWITCH = By.id("option_text_live");

    protected final UiSession session;

    protected Base64ConverterPage(UiSession session) {
        this.session = session;
    }

    /** The address of the page, from {@code taf.properties}. */
    protected abstract String url();

    public void open() {
        session.open(url());
    }

    /**
     * Switches live mode on, types the text into the input field and returns the result.
     * <p>
     * In live mode the result changes with every key stroke, so the method waits until the result is
     * <b>stable</b>: not empty and unchanged between two checks (the wait checks every 500 ms).
     */
    protected String convert(String text) {
        switchLiveModeOn();
        session.setValue(OUTPUT, "");
        session.type(INPUT, text);

        String[] previous = {null};
        return session.waitUntil(d -> {
            String current = session.getValue(OUTPUT);
            boolean stable = current != null && !current.isBlank() && current.equals(previous[0]);
            previous[0] = current;
            return stable ? current.trim() : null;
        });
    }

    /** Clicks the "Live mode OFF" switch, if live mode is not on yet, and waits until it says "ON". */
    private void switchLiveModeOn() {
        if (!session.getText(LIVE_MODE_SWITCH).endsWith("ON")) {
            session.clickElement(LIVE_MODE_SWITCH);
            session.waitUntil(d -> session.getText(LIVE_MODE_SWITCH).endsWith("ON"));
        }
    }
}
