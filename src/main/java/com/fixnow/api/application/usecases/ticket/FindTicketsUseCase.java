package com.fixnow.api.application.usecases.ticket;

import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindTicketsUseCase {

    private final TicketRepository ticketRepository;

    public List<Ticket> execute(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketRepository.findAll(pageable).getContent();
    }
}
