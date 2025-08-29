package com.farmflow.endpoint;

import com.farmflow.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
@RequestMapping("/api/v1/auth")
public interface AuthEndpoint {

    @Operation(summary = "Register a New User", description = "Creates a new user account with the provided details. Returns the created user details upon successful registration.")
    @PostMapping
    ResponseEntity<?> create(
            @Parameter(description = "User details for registration", required = true) @RequestBody UserDTO userDTO
    );

    @Operation(summary = "User Login", description = "Authenticates a user with the provided credentials and returns a JWT token or authentication response upon successful login.")
    @PostMapping("/login")
    ResponseEntity<?> login(
            @Parameter(description = "Login credentials (e.g., username and password)", required = true) @RequestBody LoginRequest loginRequest
    ) throws Exception;

    @Operation(summary = "Email Verification", description = "Verifies the user's email address by checking the provided verification code.")
    @PostMapping("/verify-email")
    ResponseEntity<?> verifyEmail(
            @Parameter(description = "Email verification details including email and verification code.", required = true)
            @RequestBody EmailVerificationRequest request) throws Exception;


    @Operation(summary = "Resend Email Verification", description = "Resends the email verification code to the provided email address.")
    @PostMapping("/resend-verification")
    ResponseEntity<?> resendVerification (
            @Parameter(description = "The email address to resend the verification code", required = true)
            @RequestBody ResendVerificationRequest request
    ) throws  Exception;


    @Operation(summary = "Change Password", description = "Changes the user's password. Requires current password and new password.")
    @PostMapping("/change-password")
    ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request
    ) throws Exception;

    @Operation(summary = "Forgot Password - Request Reset", description = "Requests a password reset by sending a reset link/token to the user's email.")
    @PostMapping("/forgot-password")
    ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ) throws Exception;

    @Operation(summary = "Forgot Password - Verify Reset", description = "Verifies the reset token and sets a new password.")
    @PostMapping("/reset-password")
    ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) throws Exception;

    @Operation(summary = "Change Email", description = "Requests an email change. Requires new email, password confirmation, and re-verification.")
    @PostMapping("/change-email")
    ResponseEntity<?> changeEmail(
            @RequestBody ChangeEmailRequest request
    ) throws Exception;
}