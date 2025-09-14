package com.farmflow.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request object for changing user password")
public class ChangePasswordRequest {

    @Schema(
            description = "Current password of the user (used for confirmation)",
            example = "oldSecurePass123",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 6, maxLength = 20
    )
    private String currentPassword;

    @Schema(
            description = "New password for the user account",
            example = "newStrongPass456",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 6, maxLength = 20
    )
    private String newPassword;
}
