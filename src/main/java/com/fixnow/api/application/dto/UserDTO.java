package com.fixnow.api.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserDTO(
        @Schema(description = "User ID", example = "104e0b78-97e4-4cc2-b139-01b7ba74b9a7")
        String id,

        @Schema(description = "User Name", example = "John")
        @NotBlank(message = "Name is mandatory")
        String name,

        @Schema(description = "Last Name", example = "Doe")
        @NotBlank(message = "Last name is mandatory")
        String lastName,

        @Schema(description = "Username for login", example = "jhon.doe")
        @NotBlank(message = "User name is mandatory")
        String userName,

        @Schema(description = "Password for user login", example = "123456")
        @NotBlank(message = "Password is mandatory")
        String password
) {
}
