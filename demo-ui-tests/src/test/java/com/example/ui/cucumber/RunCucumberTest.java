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

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectDirectories;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * Runs all Cucumber scenarios from all {@code .feature} files below {@code src/test/resources/features}
 * (including sub folders). New feature files are picked up automatically.
 *
 * <p>The class contains no code. It only tells the JUnit Platform:
 * <ul>
 *   <li>{@code @IncludeEngines("cucumber")}: use the Cucumber engine (not JUnit Jupiter),</li>
 *   <li>{@code @SelectDirectories}: in which folder (recursively) to look for {@code .feature} files,</li>
 *   <li>{@code GLUE_PROPERTY_NAME}: in which package (incl. sub packages) the step definitions and hooks live,</li>
 *   <li>{@code PLUGIN_PROPERTY_NAME}: readable console output and an HTML report in
 *       {@code target/cucumber-report.html}.</li>
 * </ul>
 *
 * <p>The path is relative to the project folder {@code demo-ui-tests}, which is the working directory
 * for {@code mvn test} as well as in IntelliJ.
 *
 * <p>Run: right-click this class → "Run 'RunCucumberTest'", or {@code mvn test}.
 */
@Suite
@IncludeEngines("cucumber")
@SelectDirectories("src/test/resources/features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.example.ui.cucumber")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-report.html")
@ConfigurationParameter(key = PLUGIN_PUBLISH_ENABLED_PROPERTY_NAME, value = "true")
class RunCucumberTest {
}
