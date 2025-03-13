package com.fixnow.api.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fixnow.api.application.dto.TicketDTO;
import com.fixnow.api.application.exception.TicketNotFoundException;
import com.fixnow.api.application.usecases.ticket.*;
import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.model.User;
import com.fixnow.api.infrastructure.mappers.TicketMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateTicketUseCase createTicketUseCase;

    @MockitoBean
    private FindTicketByIdUseCase findTicketByIdUseCase;

    @MockitoBean
    private FindTicketByUserIdUseCase findTicketByUserIdUseCase;

    @MockitoBean
    private FindTicketsUseCase findTicketsUseCase;

    @MockitoBean
    private ModifyTicketUseCase modifyTicketUseCase;

    @MockitoBean
    private FilterTicketsUseCase filterTicketsUseCase;

    private User user;
    private Ticket ticket;
    private UUID ticketId;
    private String ticketDescription;

    @BeforeEach
    public void setUp() {
        ticketId = UUID.randomUUID();
        ticketDescription = "Nuevo ticket";

        user = User.builder()
                .id(UUID.randomUUID())
                .name("John")
                .lastName("Doe")
                .userName("johndoe")
                .password("123456")
                .build();

        ticket = Ticket.builder()
                .id(ticketId)
                .description(ticketDescription)
                .user(user)
                .status(Ticket.Status.OPEN)
                .build();
    }

    @Test
    public void should_create_ticket_when_post_ticket_data() throws Exception {
        // Arrange
        TicketDTO ticketDTO = TicketMapper.toDTO(ticket);

        when(createTicketUseCase.execute(any(Ticket.class))).thenReturn(ticket);

        // Act
        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(ticketDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketId.toString()))
                .andExpect(jsonPath("$.description").value(ticketDescription));
    }

    @Test
    public void should_get_ticket_by_id() throws Exception {
        when(findTicketByIdUseCase.execute(any(UUID.class))).thenReturn(ticket);

        mockMvc.perform(get("/tickets/" + ticket.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketId.toString()));
    }

    @Test
    public void should_return_404_when_ticket_not_found_by_id() throws Exception {
        when(findTicketByIdUseCase.execute(any(UUID.class))).thenThrow(new TicketNotFoundException(ticketId.toString()));

        mockMvc.perform(get("/tickets/" + ticketId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ticket not found with id: "+ticketId.toString()));
    }

    @Test
    public void should_get_tickets_by_user_id() throws Exception {
        when(findTicketByUserIdUseCase.execute(any(UUID.class))).thenReturn(Collections.singletonList(ticket));

        mockMvc.perform(get("/tickets/user/" + user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ticketId.toString()));
    }

    @Test
    public void should_get_all_tickets() throws Exception {
        when(findTicketsUseCase.execute(any(Integer.class), any(Integer.class))).thenReturn(Collections.singletonList(ticket));

        mockMvc.perform(get("/tickets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ticketId.toString()));
    }

    @Test
    public void should_modify_ticket() throws Exception {
        TicketDTO ticketModifiedDTO = TicketDTO.builder()
                .id(ticketId.toString())
                .description("Ticket modificado")
                .userId(user.getId().toString())
                .status("CLOSED")
                .build();
        Ticket ticketModified = TicketMapper.toEntity(ticketModifiedDTO);

        when(modifyTicketUseCase.execute(any(UUID.class), any(Ticket.class))).thenReturn(ticketModified);

        mockMvc.perform(put("/tickets/" + ticket.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketModifiedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Ticket modificado"))
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    public void should_return_400_when_invalid_data_for_modify_ticket() throws Exception {
        TicketDTO invalidTicketDTO = TicketDTO.builder()
                .id(ticketId.toString())
                .description("")
                .userId(user.getId().toString())
                .status("CLOSED")
                .build();

        mockMvc.perform(put("/tickets/" + ticket.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTicketDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_filter_tickets() throws Exception {
        when(filterTicketsUseCase.execute(any(Ticket.Status.class), any(UUID.class))).thenReturn(Collections.singletonList(ticket));

        mockMvc.perform(get("/tickets/filter")
                        .param("status", "OPEN")
                        .param("userId", user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ticketId.toString()));
    }
}
