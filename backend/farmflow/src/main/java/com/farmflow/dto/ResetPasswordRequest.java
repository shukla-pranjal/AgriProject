package com.farmflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request object to reset password using a verification token")
public class ResetPasswordRequest {

    @Schema(description = "Email address of the user", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "^[A-Za-z0-9+_.-]+@(.+)$") // Adjust based on Constants.EMAIL_REGEX
    private String email;

    @Schema(description = "The verification code sent to the email for verification", example = "1A2BC3",  requiredMode = Schema.RequiredMode.REQUIRED, minLength = 6, maxLength = 6)
    private String verificationCode;

    @Schema(
            description = "New password to be set for the account",
            example = "newStrongPass456",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 8, maxLength = 30
    )
    private String newPassword;
}
