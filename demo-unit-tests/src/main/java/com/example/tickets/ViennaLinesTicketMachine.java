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

package com.example.tickets;

import de.focus_shift.jollyday.core.HolidayCalendar;
import de.focus_shift.jollyday.core.HolidayManager;
import de.focus_shift.jollyday.core.HolidayType;
import de.focus_shift.jollyday.core.ManagerParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class ViennaLinesTicketMachine {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViennaLinesTicketMachine.class);

    // ---------------------------------------------------------------------------------------------
    // Wiener Linien tariff (single trip, paper ticket from the ticket machine, valid since 01.01.2026)
    // ---------------------------------------------------------------------------------------------

    /** Einzelfahrt, full price. Senior single tickets were abolished, so seniors also pay this. */
    public static final BigDecimal SINGLE_TRIP_FULL_PRICE = new BigDecimal("3.20");

    /** Einzelfahrt, reduced price for children and youths from 6 until their 15th birthday. */
    public static final BigDecimal SINGLE_TRIP_REDUCED_PRICE = new BigDecimal("1.60");

    private final HolidayManager austrianHolidays =
            HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.AUSTRIA));

    private final ViennaSchoolHolidays schoolHolidays = new ViennaSchoolHolidays(austrianHolidays);

    public BigDecimal getSingleTripTicketPrice(int age) {
        LOGGER.debug("Calculating ticket price for age: {}", age);
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        } else if (age < 6) {
            LOGGER.debug("Age is less than 6, ticket price is free");
            return BigDecimal.ZERO; // Free for children under 6
        } else if (age < 15) {
            LOGGER.debug("Age is between 6 and 15, ticket price is reduced");
            return SINGLE_TRIP_REDUCED_PRICE; // Reduced price for children and teenagers
        } else {
            LOGGER.debug("Age is 15 or older, ticket price is standard");
            return SINGLE_TRIP_FULL_PRICE; // Standard price
        }
    }

    public Ticket getSingleTripTicket(int age, BigDecimal payment) {
        return getSingleTripTicket(age, payment, java.time.LocalDateTime.now());
    }

    /**
     * Single trip price according to the Wiener Linien rules:
     * <ul>
     *     <li>under 6: always free</li>
     *     <li>6 until the 15th birthday: free on Sundays, Austrian public holidays and Vienna school holidays,
     *         otherwise the reduced price</li>
     *     <li>15 and older: full price</li>
     * </ul>
     */
    public BigDecimal getSingleTripTicketPrice(int age, LocalDateTime dateTime) {
        LOGGER.debug("Calculating ticket price for age: {} at {}", age, dateTime);
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        if (dateTime == null) {
            throw new IllegalArgumentException("Date/time must not be null");
        }
        if (age < 6) {
            LOGGER.debug("Age is less than 6, ticket is free");
            return BigDecimal.ZERO;
        }
        if (age < 15) {
            if (isFreeTravelDayForChildren(dateTime.toLocalDate())) {
                LOGGER.debug("Sunday, public holiday or school holiday: free for children under 15");
                return BigDecimal.ZERO;
            }
            LOGGER.debug("Age is between 6 and 15, ticket price is reduced");
            return SINGLE_TRIP_REDUCED_PRICE;
        }
        LOGGER.debug("Age is 15 or older, ticket price is standard");
        return SINGLE_TRIP_FULL_PRICE;
    }

    public Ticket getSingleTripTicket(int age, BigDecimal payment, LocalDateTime dateTime) {
        BigDecimal price = getSingleTripTicketPrice(age, dateTime);
        if (payment == null || price.compareTo(payment) > 0) {
            throw new IllegalArgumentException("Insufficient payment");
        }
        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID().toString());
        ticket.setPrice(price);
        ticket.setAmountPaid(payment);
        ticket.setDateOfIssue(dateTime);
        ticket.setChange(payment.subtract(price));
        LOGGER.debug("Generated ticket with price: {} for {}", price, dateTime);
        return ticket;
    }

    boolean isFreeTravelDayForChildren(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SUNDAY
                || austrianHolidays.isHoliday(date, HolidayType.PUBLIC_HOLIDAY)
                || schoolHolidays.isSchoolHoliday(date);
    }

}
