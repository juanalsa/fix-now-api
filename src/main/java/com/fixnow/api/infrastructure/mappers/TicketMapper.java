package com.fixnow.api.infrastructure.mappers;

import com.fixnow.api.application.dto.TicketDTO;
import com.fixnow.api.domain.model.Ticket;

import java.util.UUID;

public class TicketMapper {

    public static TicketDTO toDTO(Ticket ticket) {
        return TicketDTO.builder()
                .id(ticket.getId().toString())
                .description(ticket.getDescription())
                .userId(ticket.getUser().getId().toString())
                .status(ticket.getStatus().name())
                .build();
    }

    public static Ticket toEntity(TicketDTO ticketDTO) {
        return Ticket.builder()
                .id(ticketDTO.id() != null ? UUID.fromString(ticketDTO.id()) : null)
                .description(ticketDTO.description())
                .status(ticketDTO.status() != null ? Ticket.Status.valueOf(ticketDTO.status()) : Ticket.Status.OPEN)
                .build();
    }
}
