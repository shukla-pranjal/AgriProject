package com.farmflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "DTO for email verification during authentication process.")
public class EmailVerificationRequest {

    @Schema(description = "The email address of the user to verify", example = "user@example.com")
    private String email;

    @Schema(description = "The verification code sent to the email for verification", example = "123456")
    private String verificationCode;
}