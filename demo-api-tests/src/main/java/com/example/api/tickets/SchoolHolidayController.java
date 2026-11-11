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

package com.example.api.tickets;

import com.example.tickets.ViennaSchoolHolidays;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

/**
 * REST endpoints for the Vienna school holidays used by the ticket machine.
 */
@RestController
@RequestMapping("/api/school-holidays")
@Tag(name = "School holidays", description = "Vienna school holidays and school-free days")
public class SchoolHolidayController {

    private final ViennaSchoolHolidays schoolHolidays;

    public SchoolHolidayController(ViennaSchoolHolidays schoolHolidays) {
        this.schoolHolidays = schoolHolidays;
    }

    @GetMapping("/{startYear}")
    @Operation(summary = "List all school holidays of a school year",
            description = "Example: /api/school-holidays/2026 returns school year 2026/27 (as a stream of periods).")
    public Flux<ViennaSchoolHolidays.Period> getSchoolHolidays(
            @Parameter(description = "Year in which the school year starts", example = "2026")
            @PathVariable int startYear) {
        return Mono.fromCallable(() -> schoolHolidays.getSchoolHolidays(startYear))
                .flatMapIterable(periods -> periods)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/check")
    @Operation(summary = "Check whether a date is a Vienna school holiday")
    public Mono<SchoolHolidayCheckResponse> isSchoolHoliday(
            @Parameter(description = "Date (ISO-8601)", example = "2026-12-28")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return Mono.fromCallable(() -> new SchoolHolidayCheckResponse(date, schoolHolidays.isSchoolHoliday(date)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * JSON response of {@code GET /api/school-holidays/check}.
     */
    public record SchoolHolidayCheckResponse(LocalDate date, boolean schoolHoliday) {
    }
}
