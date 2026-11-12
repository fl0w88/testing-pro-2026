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

package com.example.cucumber.calculator;

import com.example.calculator.Calculator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Step-Definitionen für die Calculator-Features.
 *
 * <p>Jeder Satz (Step) aus einer {@code .feature}-Datei wird über den Text in der Annotation
 * einer Java-Methode zugeordnet. Platzhalter wie {@code {double}} werden automatisch umgewandelt
 * und als Parameter übergeben. Die Features sind auf Englisch geschrieben, Kommazahlen
 * schreibt man daher mit Punkt, z. B. {@code 2.5}.
 *
 * <p>Cucumber legt für jedes Szenario ein neues Objekt dieser Klasse an. Die Felder
 * {@code calculator} und {@code result} gelten also immer nur für ein Szenario.
 */
public class CalculatorSteps {

    private Calculator calculator;
    private double result;

    // Arrange
    @Given("I have a calculator")
    public void iHaveACalculator() {
        calculator = new Calculator();
    }

    // Act: beliebig viele Zahlen aus einer Tabelle (eine Spalte → List<Double>)
    @When("I add the following numbers:")
    public void iAddTheFollowingNumbers(List<Double> numbers) {
        double[] values = numbers.stream().mapToDouble(Double::doubleValue).toArray();
        result = calculator.add(values);
    }

    // Act: genau zwei Zahlen direkt im Satz
    @When("I add {double} and {double}")
    public void iAddTwoNumbers(double first, double second) {
        result = calculator.add(first, second);
    }

    // Assert
    @Then("the result is {double}")
    public void theResultIs(double expected) {
        assertThat(result).isCloseTo(expected, within(1e-9));
    }
}
