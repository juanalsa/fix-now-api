package com.fixnow.api.application.usecases.ticket;

import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.repository.TicketRepository;
import com.fixnow.api.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTicketUseCase {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public Ticket execute(Ticket ticket) {
        return ticketRepository.save(ticket);
    }
}
