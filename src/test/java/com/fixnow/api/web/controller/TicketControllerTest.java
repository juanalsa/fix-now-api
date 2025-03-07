package com.fixnow.api.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fixnow.api.application.dto.TicketDTO;
import com.fixnow.api.application.usecases.ticket.CreateTicketUseCase;
import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.model.User;
import com.fixnow.api.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CreateTicketUseCase createTicketUseCase;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @Test
    public void should_create_ticket_when_post_ticket_data() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .name("John")
                .lastName("Doe")
                .userName("johndoe")
                .password("123456")
                .build();

        TicketDTO ticketDTO = TicketDTO.builder()
                .description("Nuevo ticket")
                .userId(userId.toString())
                .status(Ticket.Status.OPEN.toString())
                .build();

        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .description(ticketDTO.description())
                .user(user)
                .status(Ticket.Status.valueOf(ticketDTO.status()))
                .build();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(user));
        when(createTicketUseCase.execute(any(Ticket.class))).thenReturn(ticket);

        // Act
        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(ticketDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Nuevo Ticket"));
    }
}
