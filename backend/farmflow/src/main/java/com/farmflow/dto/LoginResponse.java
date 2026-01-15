package com.farmflow.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Response object for user login containing user details and JWT token")
public class LoginResponse {

    @Schema(description = "Details of the authenticated user", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserDTO user;

    @Schema(description = "JWT token for authentication", example = "eyJhbGciOiJIUzI1NiJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;
}