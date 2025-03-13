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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ModifyTicketUseCaseTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private ModifyTicketUseCase modifyTicketUseCase;

    private UUID ticketId;
    private Ticket existingTicket;
    private Ticket updatedTicket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ticketId = UUID.randomUUID();
        existingTicket = new Ticket();
        existingTicket.setId(ticketId);
        existingTicket.setDescription("Old Description");
        existingTicket.setStatus(Ticket.Status.OPEN);

        updatedTicket = new Ticket();
        updatedTicket.setDescription("New Description");
        updatedTicket.setStatus(Ticket.Status.CLOSED);
    }

    @Test
    void should_modify_ticket_when_data_is_valid() throws TicketNotFoundException {
        // Arrange
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(existingTicket);

        // Act
        Ticket result = modifyTicketUseCase.execute(ticketId, updatedTicket);

        // Assert
        assertNotNull(result);
        assertEquals("New Description", result.getDescription());
        assertEquals(Ticket.Status.CLOSED, result.getStatus());
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, times(1)).save(existingTicket);
    }

    @Test
    void should_throws_ticket_not_found_exception_when_ticket_does_not_exist() {
        // Arrange
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TicketNotFoundException.class, () -> {
            modifyTicketUseCase.execute(ticketId, updatedTicket);
        });

        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, times(0)).save(any(Ticket.class));
    }
}