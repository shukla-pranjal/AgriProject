package com.farmflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for resending the email verification request. Provides the user's email to send a new verification code.")
public class ResendVerificationRequest {

   @Schema(description = "The email address of the user to resend the verification code to", example = "user@example.com")
    private String email;
}