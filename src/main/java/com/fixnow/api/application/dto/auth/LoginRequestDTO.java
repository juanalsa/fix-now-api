package com.fixnow.api.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequestDTO(
        @Schema(description = "Username for the user", example = "john.doe")
        @NotBlank(message = "Username is mandatory")
        String userName,

        @Schema(description = "Password for the user", example = "123456")
        @NotBlank(message = "Password is mandatory")
        String password
) {
}
