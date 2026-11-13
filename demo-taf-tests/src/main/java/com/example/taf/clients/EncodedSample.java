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
 * One sample delivered by the (simulated) test data service, see {@link ServiceDataClient}:
 * a Base64 text and the plain text it must decode to.
 *
 * @param name         a readable key, e.g. {@code HELLO_WORLD}
 * @param base64       the encoded text
 * @param expectedText the plain text the Base64 text must decode to
 */
public record EncodedSample(String name, String base64, String expectedText) {

    /** Shown by JUnit as the name of a parameterized test case. */
    @Override
    public String toString() {
        return name;
    }
}
