package com.example.tickets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class TicketsTest {

    @Test
    public void babyTicketTest() {
        ViennaLinesTicketMachine ticketAutomat = new ViennaLinesTicketMachine();
        Ticket ticket = ticketAutomat.getSingleTripTicket(0, BigDecimal.ZERO);
        Assertions.assertNotNull(ticket);
        Assertions.assertNotNull(ticket.getId());
        System.out.println("Ticket ID: " + ticket.getId());
    }

    @Test
    public void kleinKindTicketTest() {
        ViennaLinesTicketMachine ticketAutomat = new ViennaLinesTicketMachine();
        Ticket ticket = ticketAutomat.getSingleTripTicket(5, BigDecimal.ZERO);
        Assertions.assertNotNull(ticket);
        Assertions.assertNotNull(ticket.getId());
        System.out.println("Ticket ID: " + ticket.getId());
    }

    @Test
    public void kind6TicketTest() {
        ViennaLinesTicketMachine ticketAutomat = new ViennaLinesTicketMachine();
        Ticket ticket = ticketAutomat.getSingleTripTicket(6, new BigDecimal("1.60"));
        Assertions.assertNotNull(ticket);
        Assertions.assertNotNull(ticket.getId());
        System.out.println("Ticket ID: " + ticket.getId());
    }

}
