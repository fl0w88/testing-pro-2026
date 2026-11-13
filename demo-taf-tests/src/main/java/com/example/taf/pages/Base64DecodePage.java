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
 * Layer 2 – <b>page object</b>: <a href="https://www.base64decode.org/">base64decode.org</a>
 * (the sister site of base64encode.org, with the same form).
 */
public class Base64DecodePage extends Base64ConverterPage {

    public Base64DecodePage(UiSession session) {
        super(session);
    }

    @Override
    protected String url() {
        return TafConfig.base64DecodeUrl();
    }

    /** Decodes the Base64 text on the website and returns the plain text. */
    public String decode(String base64) {
        return convert(base64);
    }
}
