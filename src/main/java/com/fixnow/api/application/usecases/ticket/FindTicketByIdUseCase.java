package com.fixnow.api.application.usecases.ticket;

import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.repository.TicketRepository;
import com.fixnow.api.application.exception.TicketNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindTicketByIdUseCase {

    private final TicketRepository ticketRepository;

    public Ticket execute(UUID id) throws TicketNotFoundException {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id.toString()));
    }
}
