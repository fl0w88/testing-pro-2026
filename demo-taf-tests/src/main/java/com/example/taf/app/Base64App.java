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

package com.example.taf.app;

import com.example.taf.core.UiSession;
import com.example.taf.keywords.Base64Keywords;

/**
 * Layer 4 – <b>application</b>: the entry point for one application under test.
 *
 * <p>It groups all keyword sets of the application. Today there is one ({@link #base64Keywords()}); a
 * bigger application would have several, e.g. {@code loginKeywords()}, {@code searchKeywords()}, ...
 * A test reads like: {@code base64App().base64Keywords().encodeText("...")}.
 */
public class Base64App {

    private final Base64Keywords base64Keywords;

    public Base64App(UiSession session) {
        this.base64Keywords = new Base64Keywords(session);
    }

    public Base64Keywords base64Keywords() {
        return base64Keywords;
    }
}
