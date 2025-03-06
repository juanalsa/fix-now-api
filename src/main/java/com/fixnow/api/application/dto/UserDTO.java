package com.fixnow.api.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserDTO(
        String id,

        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Last name is mandatory")
        String lastName
) {
}
