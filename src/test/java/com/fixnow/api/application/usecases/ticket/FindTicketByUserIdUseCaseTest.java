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

class FindTicketByUserIdUseCaseTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private FindTicketByUserIdUseCase findTicketByUserIdUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_return_tickets_when_user_id_exists() {
        UUID userId = UUID.randomUUID();
        List<Ticket> expectedTickets = List.of(new Ticket(), new Ticket());

        when(ticketRepository.findByUserId(userId)).thenReturn(expectedTickets);

        List<Ticket> actualTickets = findTicketByUserIdUseCase.execute(userId);

        assertEquals(expectedTickets, actualTickets);
    }

    @Test
    void should_return_empty_list_when_user_id_does_not_exist() {
        UUID userId = UUID.randomUUID();

        when(ticketRepository.findByUserId(userId)).thenReturn(List.of());

        List<Ticket> actualTickets = findTicketByUserIdUseCase.execute(userId);

        assertEquals(0, actualTickets.size());
    }
}