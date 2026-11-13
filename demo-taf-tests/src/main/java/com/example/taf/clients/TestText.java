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

/**
 * One row of the (simulated) database table {@code TEST_TEXTS}, delivered by the {@link DBClient}.
 *
 * @param id          primary key
 * @param name        a readable key, e.g. {@code SIMPLE_SENTENCE}
 * @param text        the plain text to encode
 */
public record TestText(int id, String name, String text) {

    /** Shown by JUnit as the name of a parameterized test case. */
    @Override
    public String toString() {
        return name;
    }
}
