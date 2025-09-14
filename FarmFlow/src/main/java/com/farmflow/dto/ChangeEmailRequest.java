package com.farmflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request object to change the user's registered email address")
public class ChangeEmailRequest {

    @Schema(
            description = "Current password of the user (required for confirmation before changing email)",
            example = "securePass123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String currentPassword;

    @Schema(
            description = "New email address to be linked with the account",
            example = "new.email@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "^[A-Za-z0-9+_.-]+@(.+)$"
    )
    private String newEmail;
}
