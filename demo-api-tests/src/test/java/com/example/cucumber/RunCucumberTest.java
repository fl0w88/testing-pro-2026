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

package com.example.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectDirectories;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;


/**
 * Startet alle Cucumber-Szenarien aus allen {@code .feature}-Dateien unter {@code src/test/resources}
 * (auch in Unterordnern). Neue Feature-Dateien werden also automatisch mit ausgeführt.
 *
 * <p>Die Klasse selbst enthält keinen Code. Sie sagt der JUnit Platform nur:
 * <ul>
 *   <li>{@code @IncludeEngines("cucumber")}: die Cucumber-Engine verwenden (nicht JUnit Jupiter),</li>
 *   <li>{@code @SelectDirectories}: in welchem Ordner (rekursiv) die {@code .feature}-Dateien gesucht werden,</li>
 *   <li>{@code GLUE_PROPERTY_NAME}: in welchem Package (inkl. Unterpackages) die Step-Definitionen liegen.</li>
 * </ul>
 *
 * <p>Der Pfad ist relativ zum Projektordner {@code demo-api-tests}. Das ist das Arbeitsverzeichnis
 * sowohl bei {@code mvn test} als auch in IntelliJ.
 *
 * <p>Ausführen: Rechtsklick auf diese Klasse → „Run 'RunCucumberTest'“ oder {@code mvn test}.
 */
@Suite
@IncludeEngines("cucumber")
@SelectDirectories("src/test/resources")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.example")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@ConfigurationParameter(key = PLUGIN_PUBLISH_ENABLED_PROPERTY_NAME, value = "true")
class RunCucumberTest {
}
