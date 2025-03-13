package com.fixnow.api.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDTO(
        @Schema(description = "Token generated for the user", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjZ29tZXoiLCJpYXQiOjE3NDEzMzIwNDEsImV4cCI6MTc0MTMzMzg0MX0.7Vturpdr01WhSTSATmU1gdJqKGMQiTaegtn_IM_zw08")
        String jwt
) {
}
