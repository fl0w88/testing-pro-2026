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

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * General information shown at the top of the Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI demoOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Demo API – Calculator & Vienna Lines Ticket Machine")
                .version("1.0.0")
                .description("Training API for the course \"Einführung in die Testautomatisierung\". "
                        + "All endpoints are reactive (Spring WebFlux) and non-blocking.")
                .contact(new Contact().name("Florin Balint, Nagarro GmbH"))
                .license(new License().name("Apache License 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
