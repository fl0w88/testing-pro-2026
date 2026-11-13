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
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * General steps that work for every website: choose the website, open pages, check the title.
 *
 * <p>Each sentence (step) in a {@code .feature} file is matched to a Java method by the text in the
 * annotation. Placeholders such as {@code {word}} or {@code {string}} are converted and passed as parameters.
 *
 * <p>The {@link UiSession} (browser + actions) is injected by PicoContainer – see
 * {@code com.example.ui.cucumber.BrowserHooks}.
 */
public class WebsiteSteps {

    /** The websites the feature files can refer to by name. Add new websites here. */
    private static final Map<String, String> WEBSITES = Map.of(
            "PENNY", "https://www.penny.at/");

    private final UiSession session;

    public WebsiteSteps(UiSession session) {
        this.session = session;
    }

    // Arrange: which website is tested?
    @Given("I want to verify the {word} website")
    public void iWantToVerifyTheWebsite(String name) {
        String url = WEBSITES.get(name);
        if (url == null) {
            throw new IllegalArgumentException("Unknown website '" + name + "'. Known websites: " + WEBSITES.keySet()
                    + " – add it to WebsiteSteps.WEBSITES");
        }
        session.setBaseUrl(url);
    }

    // Act
    @When("I call the startpage")
    public void iCallTheStartpage() {
        session.open();
    }

    @When("I call the page {string}")
    public void iCallThePage(String path) {
        session.open(path);
    }

    // Assert
    @Then("the page has a title")
    public void thePageHasATitle() {
        assertThat(session.waitForTitle()).isNotBlank();
    }

    @Then("the page title contains {string}")
    public void thePageTitleContains(String text) {
        session.waitForTitleContains(text);
        String pageTitle = null;
        // TODO Exercise 3: get the page title and assert that it contains the expected text (see README)
        assertThat(pageTitle).contains(text);
    }
}
