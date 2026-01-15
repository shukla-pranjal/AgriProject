package com.farmflow.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request object for user login")
public class LoginRequest {

    @Schema(description = "Email address of the user", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "^[A-Za-z0-9+_.-]+@(.+)$") // Adjust based on Constants.EMAIL_REGEX
    private String email;

    @Schema(description = "Password for the user account", example = "securePass123", requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 6, maxLength = 20)
    private String password;
}