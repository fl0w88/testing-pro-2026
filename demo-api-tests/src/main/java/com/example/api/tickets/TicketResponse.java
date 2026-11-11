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

import com.example.tickets.Ticket;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JSON representation of a {@link Ticket} returned by {@code POST /api/tickets}.
 */
public record TicketResponse(String id, BigDecimal price, BigDecimal amountPaid, BigDecimal change,
                             String currency, LocalDateTime dateOfIssue) {

    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(ticket.getId(), ticket.getPrice(), ticket.getAmountPaid(), ticket.getChange(),
                ticket.getCurrency().getCurrencyCode(), ticket.getDateOfIssue());
    }
}
