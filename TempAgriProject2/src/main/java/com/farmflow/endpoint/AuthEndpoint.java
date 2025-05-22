package com.farmflow.endpoint;

import com.farmflow.dto.LoginRequest;
import com.farmflow.dto.UserDTO;
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
    );
}