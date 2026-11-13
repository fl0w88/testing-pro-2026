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

package com.example.taf.base;

import com.example.taf.app.Base64App;
import com.example.taf.clients.DBClient;
import com.example.taf.clients.ServiceDataClient;
import com.example.taf.core.UiSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for all test cases. A test class extends it and gets everything it needs:
 * <ul>
 *   <li>{@link #base64App()} – the application under test, with its keywords,</li>
 *   <li>{@link #dbClient()} and {@link #serviceDataClient()} – where the test data comes from.</li>
 * </ul>
 * Before every test a fresh browser is started, after every test it is closed – even if the test fails.
 */
public abstract class BaseTafTest {

    private final DBClient dbClient = new DBClient();
    private final ServiceDataClient serviceDataClient = new ServiceDataClient();

    private UiSession session;
    private Base64App base64App;

    @BeforeEach
    void startBrowser() {
        session = new UiSession();
        session.startBrowser();
        base64App = new Base64App(session);
    }

    @AfterEach
    void closeBrowser() {
        if (session != null) {
            session.closeBrowser();
        }
    }

    /** The Base64 application (base64encode.org / base64decode.org) and its keywords. */
    protected Base64App base64App() {
        return base64App;
    }

    /** Test data from the (simulated) database. */
    protected DBClient dbClient() {
        return dbClient;
    }

    /** Test data from the (simulated) test data service. */
    protected ServiceDataClient serviceDataClient() {
        return serviceDataClient;
    }
}
