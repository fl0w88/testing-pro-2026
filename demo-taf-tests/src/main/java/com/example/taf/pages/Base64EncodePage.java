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

package com.example.taf.pages;

import com.example.taf.config.TafConfig;
import com.example.taf.core.UiSession;

/**
 * Layer 2 – <b>page object</b>: <a href="https://www.base64encode.org/">base64encode.org</a>.
 */
public class Base64EncodePage extends Base64ConverterPage {

    public Base64EncodePage(UiSession session) {
        super(session);
    }

    @Override
    protected String url() {
        return TafConfig.base64EncodeUrl();
    }

    /** Encodes the text on the website and returns the Base64 result. */
    public String encode(String text) {
        return convert(text);
    }
}
