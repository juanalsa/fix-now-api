package com.fixnow.api.application.usecases.ticket;

import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilterTicketsUseCase {

    private final TicketRepository ticketRepository;

    public List<Ticket> execute(Ticket.Status status, UUID userId) {
        List<Ticket> tickets;

        if (status != null && userId != null) {
            tickets = ticketRepository.findByStatusAndUserId(status, userId);
        } else if (status != null) {
            tickets = ticketRepository.findByStatus(status);
        } else if (userId != null) {
            tickets = ticketRepository.findByUserId(userId);
        } else {
            tickets = ticketRepository.findAll();
        }

        return tickets;
    }
}
