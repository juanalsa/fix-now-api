package com.fixnow.api.application.usecases.ticket;

import com.fixnow.api.application.exception.UserNotFoundException;
import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.model.User;
import com.fixnow.api.domain.repository.TicketRepository;
import com.fixnow.api.domain.repository.UserRepository;
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

public class CreateTicketUseCaseTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateTicketUseCase createTicketUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_create_ticket_when_data_is_valid() throws UserNotFoundException {
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

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(user));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Act
        Ticket createdTicket = createTicketUseCase.execute(ticket);

        // Assert
        assertNotNull(createdTicket);
        assertEquals("Nuevo ticket", createdTicket.getDescription());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    public void should_throw_UserNotFoundException_when_user_does_not_exist() {
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

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> createTicketUseCase.execute(ticket));
        verify(ticketRepository, never()).save(any(Ticket.class));
    }
}
