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

package com.example.api.config;

import com.example.calculator.Calculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Makes the plain Java {@link Calculator} from the unit test project available as a Spring bean,
 * so the controller can get it injected. The domain class itself stays unchanged.
 */
@Configuration
public class DomainConfig {

    @Bean
    public Calculator calculator() {
        return new Calculator();
    }
}
