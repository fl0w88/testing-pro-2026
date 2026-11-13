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

package com.example.taf.tests;

import com.example.taf.base.BaseTafTest;
import com.example.taf.clients.EncodedSample;
import com.example.taf.config.TafConfig;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Exercise 2 (see README): test cases for decoding on base64decode.org.
 *
 * <p>The test data is already there: the Base64 input and the expected plain text come from the
 * (simulated) test data service ({@code serviceDataClient()}), the original text for the round trip
 * from the (simulated) database ({@code dbClient()}). Opening the site and checking the result is
 * up to you – with the keywords from {@code base64App().base64Keywords()}.
 *
 * <p>base64decode.org shows a bot check to headless browsers, so these tests only run with a visible
 * browser ({@code browser.headless=false}, the default) and are skipped otherwise.
 */
class Base64DecodeTest extends BaseTafTest {

    @BeforeEach
    void onlyWithVisibleBrowser() {
        Assumptions.assumeFalse(TafConfig.isHeadless(),
                "base64decode.org shows a bot check to headless browsers – run with -Dbrowser.headless=false");
    }

    @Test
    @DisplayName("Exercise 2: decode a sample from the test data service")
    void decodeSampleFromService() {
        EncodedSample sample = serviceDataClient().getEncodedSample("HELLO_WORLD");

        // TODO open base64decode.org and close the cookie dialog
        // TODO decode sample.base64() on the website
        // TODO check that the result is sample.expectedText()
        fail("Exercise 2: not implemented yet");
    }

    @Test
    @DisplayName("Exercise 2: round trip – encode a database text, decode it again, get the original text")
    void encodeAndDecodeRoundTrip() {
        String originalText = dbClient().getTestText("SIMPLE_SENTENCE").text();

        // TODO encode originalText on base64encode.org
        // TODO decode the result on base64decode.org
        // TODO check that the decoded text is originalText again
        fail("Exercise 2: not implemented yet");
    }
}
