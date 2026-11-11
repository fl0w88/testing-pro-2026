package com.example.tickets;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

/**
 * Ticket class representing a ticket with an ID, price, and date of issue.
 * Simple abstraction for demonstration purposes.
 */
public class Ticket {

    private String id;
    private BigDecimal price;

    private BigDecimal amountPaid;

    private BigDecimal change;

    private Currency currency = Currency.getInstance("EUR");

    private final LocalDateTime dateOfIssue = LocalDateTime.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getDateOfIssue() {
        return dateOfIssue;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    public Currency getCurrency() {
        return currency;
    }

}
