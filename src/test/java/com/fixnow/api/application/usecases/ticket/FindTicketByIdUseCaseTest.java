package com.fixnow.api.application.usecases.ticket;

import com.fixnow.api.application.exception.TicketNotFoundException;
import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindTicketByIdUseCaseTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private FindTicketByIdUseCase findTicketByIdUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_return_ticket_when_ticket_exists() throws TicketNotFoundException {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        Ticket ticket = Ticket.builder()
                .id(ticketId)
                .description("Existing ticket")
                .status(Ticket.Status.OPEN)
                .build();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        // Act
        Ticket foundTicket = findTicketByIdUseCase.execute(ticketId);

        // Assert
        assertNotNull(foundTicket);
        assertEquals(ticketId, foundTicket.getId());
        assertEquals("Existing ticket", foundTicket.getDescription());
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    public void should_throw_TicketNotFoundException_when_ticket_does_not_exist() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TicketNotFoundException.class, () -> findTicketByIdUseCase.execute(ticketId));
        verify(ticketRepository, times(1)).findById(ticketId);
    }
}