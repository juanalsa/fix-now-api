package com.fixnow.api.application.usecases.ticket;

import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.repository.TicketRepository;
import com.fixnow.api.web.exception.TicketNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModifyTicketUseCase {

    private final TicketRepository ticketRepository;

    public Ticket execute(UUID id, Ticket ticketData) throws TicketNotFoundException {
        System.out.println("id: " + id);
        System.out.println("ticketData: " + ticketData);

        Ticket ticketFound = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id.toString()));

        ticketFound.setDescription(ticketData.getDescription());
        //ticketFound.setUser(ticketData.getUser());
        ticketFound.setStatus(ticketData.getStatus());
        ticketRepository.save(ticketFound);

        return ticketFound;
    }
}
