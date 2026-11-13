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

package com.example.ui.cucumber.steps;

import com.example.ui.base.UiSession;
import com.example.ui.components.UsercentricsCookieBanner;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Steps for the cookie prompt. The websites in this project (penny.at) use the Usercentrics banner,
 * so these steps use the reusable {@link UsercentricsCookieBanner} component.
 */
public class CookieSteps {

    private final UsercentricsCookieBanner cookieBanner;

    public CookieSteps(UiSession session) {
        this.cookieBanner = new UsercentricsCookieBanner(session);
    }

    @Then("the cookie prompt is loaded")
    public void theCookiePromptIsLoaded() {
        assertThat(cookieBanner.waitUntilLoaded().isDisplayed()).isTrue();
    }

    @When("I accept all cookies")
    public void iAcceptAllCookies() {
        cookieBanner.acceptAll();
    }

    @Then("the cookie prompt is closed")
    public void theCookiePromptIsClosed() {
        assertThat(cookieBanner.isOpen()).isFalse();
    }
}
