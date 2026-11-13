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

package com.example.taf.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Reads the framework configuration from {@code src/main/resources/taf.properties}.
 *
 * <p>A system property with the same name wins over the file, so every value can be changed without
 * touching code: {@code mvn test -Dbrowser.headless=true}.
 */
public final class TafConfig {

    private static final String FILE = "taf.properties";
    private static final Properties PROPERTIES = load();

    private TafConfig() {
    }

    /** The value for {@code key}: first from the system properties, then from {@code taf.properties}. */
    public static String get(String key) {
        String value = System.getProperty(key, PROPERTIES.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing configuration '" + key + "' in " + FILE);
        }
        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    // ---- typed shortcuts for the values the framework uses ----

    public static String base64EncodeUrl() {
        return get("base64.encode.url");
    }

    public static String base64DecodeUrl() {
        return get("base64.decode.url");
    }

    public static boolean isHeadless() {
        return getBoolean("browser.headless");
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream in = TafConfig.class.getClassLoader().getResourceAsStream(FILE)) {
            if (in == null) {
                throw new IllegalStateException(FILE + " not found on the classpath (src/main/resources)");
            }
            properties.load(in);
            return properties;
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + FILE, e);
        }
    }
}
