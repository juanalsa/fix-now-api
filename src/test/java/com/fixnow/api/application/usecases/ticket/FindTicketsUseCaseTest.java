package com.fixnow.api.application.usecases.ticket;

import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindTicketsUseCaseTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private FindTicketsUseCase findTicketsUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_return_all_paginated_tickets_when_execute() {
        // Arrange
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<Ticket> tickets = Arrays.asList(new Ticket(), new Ticket());
        Page<Ticket> ticketPage = new PageImpl<>(tickets, pageable, tickets.size());

        when(ticketRepository.findAll(pageable)).thenReturn(ticketPage);

        // Act
        List<Ticket> result = findTicketsUseCase.execute(page, size);

        // Assert
        assertEquals(tickets.size(), result.size());
        verify(ticketRepository, times(1)).findAll(pageable);
    }
}