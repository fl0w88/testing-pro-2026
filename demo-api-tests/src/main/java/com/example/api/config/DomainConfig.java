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

import com.example.Calculator;
import com.example.tickets.ViennaLinesTicketMachine;
import com.example.tickets.ViennaSchoolHolidays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Makes the plain Java classes from the unit test project available as Spring beans,
 * so the controllers can get them injected. The domain classes themselves stay unchanged.
 */
@Configuration
public class DomainConfig {

    @Bean
    public Calculator calculator() {
        return new Calculator();
    }

    @Bean
    public ViennaLinesTicketMachine viennaLinesTicketMachine() {
        return new ViennaLinesTicketMachine();
    }

    @Bean
    public ViennaSchoolHolidays viennaSchoolHolidays() {
        return new ViennaSchoolHolidays();
    }
}
