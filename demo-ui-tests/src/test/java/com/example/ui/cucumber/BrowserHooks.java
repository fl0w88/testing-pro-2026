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

package com.example.ui.cucumber;

import com.example.ui.base.UiSession;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * Cucumber hooks: the equivalent of {@code @BeforeEach}/{@code @AfterEach} for scenarios.
 *
 * <p><b>How do the step classes share one browser?</b> Cucumber uses <b>PicoContainer</b>
 * (dependency {@code cucumber-picocontainer}): for every scenario it creates exactly one {@link UiSession}
 * and passes the same instance into the constructor of every class that asks for it – this hook class
 * and all step classes. After the scenario everything is thrown away, so scenarios do not influence
 * each other.
 */
public class BrowserHooks {

    private final UiSession session;

    public BrowserHooks(UiSession session) {
        this.session = session;
    }

    @Before
    public void startBrowser() {
        session.startBrowser();
    }

    /** If the scenario failed, attach a screenshot to the report – then close the browser. */
    @After
    public void closeBrowser(Scenario scenario) {
        try {
            if (scenario.isFailed() && session.getDriver() != null) {
                byte[] screenshot = ((TakesScreenshot) session.getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "screenshot of the failed step");
            }
        } finally {
            session.closeBrowser();
        }
    }
}
