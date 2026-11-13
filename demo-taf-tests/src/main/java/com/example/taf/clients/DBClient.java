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

package com.example.taf.clients;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Data client: <b>database</b> – delivers plain texts to encode.
 *
 * <p><b>Simulated:</b> there is no real database connection. The "table" {@code TEST_TEXTS} is a list
 * in memory. In a real project this class would open a JDBC connection and run e.g.
 * {@code SELECT id, name, text FROM test_texts WHERE name = ?}. The tests would not notice the
 * difference – that is the point of a client: the test asks for data, the client knows where it comes from.
 */
public class DBClient {

    /** Simulated table TEST_TEXTS. */
    private static final List<TestText> TEST_TEXTS = List.of(
            new TestText(1, "SIMPLE_SENTENCE", "Example text that we will encode"),
            new TestText(2, "SINGLE_WORD", "Nagarro"),
            new TestText(3, "NUMBERS_AND_SYMBOLS", "Order #4711: 3 x 19.99 EUR = 59.97 EUR!"),
            new TestText(4, "UMLAUTS", "Grüße aus Wien – Straße, Käse, Öl"),
            new TestText(5, "JSON", "{\"user\":\"tester\",\"active\":true}"));

    /** Like {@code SELECT * FROM test_texts WHERE name = ?}. */
    public TestText getTestText(String name) {
        return TEST_TEXTS.stream()
                .filter(row -> row.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No row in TEST_TEXTS with name '" + name + "'"));
    }

    /** Like {@code SELECT * FROM test_texts WHERE id = ?}. */
    public TestText getTestText(int id) {
        return TEST_TEXTS.stream()
                .filter(row -> row.id() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No row in TEST_TEXTS with id " + id));
    }

    /** Like {@code SELECT * FROM test_texts ORDER BY id}. */
    public List<TestText> getAllTestTexts() {
        return TEST_TEXTS;
    }
}
