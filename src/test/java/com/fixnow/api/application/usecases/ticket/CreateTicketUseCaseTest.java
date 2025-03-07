package com.fixnow.api.application.usecases.ticket;

import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.model.User;
import com.fixnow.api.domain.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateTicketUseCaseTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private CreateTicketUseCase createTicketUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_create_ticket_when_data_is_valid() {
        // Arrange
        User user = User.builder()
                .id(UUID.randomUUID())
                .name("John")
                .lastName("Doe")
                .userName("johndoe")
                .password("123456")
                .build();

        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .description("Nuevo ticket")
                .user(user)
                .status(Ticket.Status.OPEN)
                .build();

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Act
        Ticket createdTicket = createTicketUseCase.execute(ticket);

        // Assert
        assertNotNull(createdTicket);
        assertEquals("Nuevo ticket", createdTicket.getDescription());
        verify(ticketRepository, times(1)).save(ticket);
    }
}
