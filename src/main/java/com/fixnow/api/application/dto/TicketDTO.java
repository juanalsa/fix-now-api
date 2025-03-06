package com.fixnow.api.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TicketDTO(
        String id,

        @NotBlank(message = "Description is mandatory")
        @Size(max = 500, message = "Description must be less than 500 characters")
        String description,

        @NotBlank(message = "User ID is mandatory")
        String userId,

        String status
) {
}
