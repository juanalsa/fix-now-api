package com.fixnow.api.application.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank String userName,
        @NotBlank String password
) {
}
