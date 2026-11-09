/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package com.example;


import org.junit.jupiter.api.*;

@Tag("development")
@Tag("stable")
public class CalculatorTest {

    // we define a variable ( or an object) of the type Calculator
    private Calculator myCalculator;

    // before we start, we must initialize our Calculator
    @BeforeEach
    public void init() {
        // this is how we 'create' a new Calculator
        myCalculator = new Calculator();
    }

    @Test
    @DisplayName("My 1st JUnit 5 test! 😎 - Calculator - Addition Test")
    public void additionTest() {
        Assertions.assertEquals(3, myCalculator.add(1, 2));
    }

}
