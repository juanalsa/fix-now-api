package com.fixnow.api.application.usecases.ticket;

import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class FilterTicketsUseCaseTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private FilterTicketsUseCase filterTicketsUseCase;

    private UUID userId;
    private Ticket.Status status;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        status = Ticket.Status.OPEN;
    }

    @Test
    void should_return_tickets_when_search_by_status_and_user_id() {
        List<Ticket> expectedTickets = List.of(new Ticket());
        when(ticketRepository.findByStatusAndUserId(status, userId)).thenReturn(expectedTickets);

        List<Ticket> actualTickets = filterTicketsUseCase.execute(status, userId);

        assertEquals(expectedTickets, actualTickets);
    }

    @Test
    void should_return_tickets_when_search_by_status() {
        List<Ticket> expectedTickets = List.of(new Ticket());
        when(ticketRepository.findByStatus(status)).thenReturn(expectedTickets);

        List<Ticket> actualTickets = filterTicketsUseCase.execute(status, null);

        assertEquals(expectedTickets, actualTickets);
    }

    @Test
    void should_return_tickets_when_search_by_user_id() {
        List<Ticket> expectedTickets = List.of(new Ticket());
        when(ticketRepository.findByUserId(userId)).thenReturn(expectedTickets);

        List<Ticket> actualTickets = filterTicketsUseCase.execute(null, userId);

        assertEquals(expectedTickets, actualTickets);
    }

    @Test
    void should_return_tickets_when_search_with_no_filters() {
        List<Ticket> expectedTickets = List.of(new Ticket());
        when(ticketRepository.findAll()).thenReturn(expectedTickets);

        List<Ticket> actualTickets = filterTicketsUseCase.execute(null, null);

        assertEquals(expectedTickets, actualTickets);
    }
}