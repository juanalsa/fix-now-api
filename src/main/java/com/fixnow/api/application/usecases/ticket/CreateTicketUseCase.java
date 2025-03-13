package com.fixnow.api.application.usecases.ticket;

import com.fixnow.api.application.exception.UserNotFoundException;
import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.model.User;
import com.fixnow.api.domain.repository.TicketRepository;
import com.fixnow.api.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateTicketUseCase {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public Ticket execute(Ticket ticket) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(ticket.getUser().getId());

        if (user.isEmpty()) {
            throw new UserNotFoundException(ticket.getUser().getId().toString());
        }

        return ticketRepository.save(ticket);
    }
}
