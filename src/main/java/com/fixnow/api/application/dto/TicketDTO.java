package com.fixnow.api.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TicketDTO(
        @Schema(description = "Ticket ID", example = "a36a7cfa-4bb2-4810-a528-9c151f62f4e2")
        String id,

        @Schema(description = "Description for the ticket", example = "Test ticket")
        @NotBlank(message = "Description is mandatory")
        @Size(max = 500, message = "Description must be less than 500 characters")
        String description,

        @Schema(description = "User associated to the ticket", example = "104e0b78-97e4-4cc2-b139-01b7ba74b9a7")
        @NotBlank(message = "User ID is mandatory")
        String userId,

        @Schema(description = "Ticket status", example = "CLOSED")
        String status
) {
}
