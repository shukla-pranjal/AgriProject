package com.farmflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request object to initiate a password reset process")
public class ForgotPasswordRequest {

    @Schema(
            description = "Email address of the user requesting password reset",
            example = "john.doe@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "^[A-Za-z0-9+_.-]+@(.+)$"
    )
    private String email;
}