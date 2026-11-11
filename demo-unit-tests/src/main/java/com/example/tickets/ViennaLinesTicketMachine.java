package com.example.tickets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.UUID;

public class ViennaLinesTicketMachine {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViennaLinesTicketMachine.class);

    public BigDecimal getSingleTripTicketPrice(int age) {
        LOGGER.debug("Calculating ticket price for age: {}", age);
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        } else if (age < 6) {
            LOGGER.debug("Age is less than 6, ticket price is free");
            return BigDecimal.ZERO; // Free for children under 6
        } else if (age < 18) {
            LOGGER.debug("Age is between 6 and 18, ticket price is reduced");
            return new BigDecimal("5"); // Reduced price for children and teenagers
        } else {
            LOGGER.debug("Age is 18 or older, ticket price is standard");
            return new BigDecimal("10"); // Standard price
        }
    }

    public Ticket getSingleTripTicket(int age, BigDecimal payment) {
        BigDecimal price = getSingleTripTicketPrice(age);
        if (price.compareTo(payment) > 0) {
            throw new IllegalArgumentException("Insufficient payment");
        }
        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID().toString());
        ticket.setPrice(price);
        ticket.setAmountPaid(payment);
        ticket.setChange(payment.subtract(price));
        LOGGER.debug("Generated ticket with price: {}", price);
        return ticket;
    }

}
