package com.fixnow.api.application.usecases.ticket;

import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindTicketByUserIdUseCase {

    private final TicketRepository ticketRepository;

    @Cacheable(value = "tickets", key = "#userId")
    public List<Ticket> execute(UUID userId) {
        return ticketRepository.findByUserId(userId);
    }
}
