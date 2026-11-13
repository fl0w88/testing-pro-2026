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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Test cases for encoding on base64encode.org.
 *
 * <p>The test cases only use keywords ({@code base64App().base64Keywords()...}) and data clients
 * ({@code dbClient()}) – no Selenium, no locators. The expected result is calculated with Java's
 * own {@link Base64} encoder: the website must produce the same.
 */
class Base64EncodeTest extends BaseTafTest {

    @Test
    @DisplayName("Encode a text from the database")
    void encodeTextFromDatabase() {
        base64App().base64Keywords().openBase64Site();

        base64App().base64Keywords().closeCookies();

        String textToEncode = dbClient().getTestText("UMLAUTS").text();
        String actualResult = base64App().base64Keywords().encodeText(textToEncode);

        // Java Base64 conversion
        String expectedResult = Base64.getEncoder().encodeToString(textToEncode.getBytes(StandardCharsets.UTF_8));

        Assertions.assertEquals(expectedResult, actualResult);
    }
}
