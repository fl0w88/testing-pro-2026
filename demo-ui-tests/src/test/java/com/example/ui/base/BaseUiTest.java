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

package com.example.ui.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Common base class for all JUnit UI tests.
 *
 * <p>A new test class only has to extend this class and name its website (domain):
 * <pre>{@code
 * class MyWebsiteTest extends BaseUiTest {
 *
 *     MyWebsiteTest() {
 *         super("https://www.example.com/");
 *     }
 *
 *     @Test
 *     void startPageHasTitle() {
 *         open();
 *         assertThat(driver.getTitle()).isEqualTo("Example Domain");
 *     }
 * }
 * }</pre>
 *
 * <p>All actions ({@code open}, {@code clickElement}, {@code waitForVisible}, {@code getText}, ...) are
 * inherited from {@link UiSession}. This class only adds the JUnit lifecycle: a fresh Chrome before
 * every test, closed again after every test – even if the test fails.
 */
public abstract class BaseUiTest extends UiSession {

    /**
     * @param baseUrl start address of the website, e.g. {@code https://www.penny.at/}
     */
    protected BaseUiTest(String baseUrl) {
        setBaseUrl(baseUrl);
    }

    @BeforeEach
    void startBrowserBeforeEachTest() {
        startBrowser();
    }

    @AfterEach
    void closeBrowserAfterEachTest() {
        closeBrowser();
    }
}
