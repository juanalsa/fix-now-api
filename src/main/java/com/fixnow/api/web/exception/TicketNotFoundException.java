package com.fixnow.api.web.exception;

public class TicketNotFoundException extends Exception {
    public TicketNotFoundException(String id) {
        super("Ticket not found with id: " + id);
    }
}
