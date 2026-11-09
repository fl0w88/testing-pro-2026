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

import java.util.Arrays;
import java.util.List;


public class WordSorterTest {

    @Test
    public void test1() {
        List<String> toSort = Arrays.asList("Maria", "Ana", "Laura");

        List<String> result = WordSorter.sortWords(toSort);

        Assertions.assertEquals("Ana", result.get(0));
        Assertions.assertEquals("Laura", result.get(1));
        Assertions.assertEquals("Maria", result.get(2));
    }

}
