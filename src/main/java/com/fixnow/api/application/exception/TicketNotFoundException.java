package com.fixnow.api.application.exception;

public class TicketNotFoundException extends Exception {
    public TicketNotFoundException(String id) {
        super("Ticket not found with id: " + id);
    }
}
