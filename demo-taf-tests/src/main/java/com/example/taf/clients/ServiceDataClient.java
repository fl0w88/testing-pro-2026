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
 * Data client: <b>test data service</b> – delivers Base64 samples to decode, together with the
 * expected plain text.
 *
 * <p><b>Simulated:</b> there is no real service call. In a real project this class would call a REST
 * endpoint, e.g. {@code GET https://testdata.example.com/api/base64-samples/{name}}, and turn the JSON
 * response into an {@link EncodedSample}. The tests would not notice the difference.
 */
public class ServiceDataClient {

    /** Simulated response of the service. */
    private static final List<EncodedSample> SAMPLES = List.of(
            new EncodedSample("HELLO_WORLD", "SGVsbG8gV29ybGQ=", "Hello World"),
            new EncodedSample("TEST_AUTOMATION", "VGVzdCBhdXRvbWF0aW9uIGlzIGZ1bg==", "Test automation is fun"),
            new EncodedSample("UMLAUTS", "R3LDvMOfZSBhdXMgV2llbg==", "Grüße aus Wien"));

    /** Like {@code GET /api/base64-samples/{name}}. */
    public EncodedSample getEncodedSample(String name) {
        return SAMPLES.stream()
                .filter(sample -> sample.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("The service has no sample '" + name + "'"));
    }

    /** Like {@code GET /api/base64-samples}. */
    public List<EncodedSample> getAllEncodedSamples() {
        return SAMPLES;
    }
}
