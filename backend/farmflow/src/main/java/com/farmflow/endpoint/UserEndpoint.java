package com.farmflow.endpoint;


import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.UserDTO;
import com.farmflow.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Management", description = "Endpoints for managing users")
@RequestMapping("/api/v1/users")
public interface UserEndpoint {

    @Operation(summary = "Get All Users", description = "Retrieves a list of all users in the system. Accessible by admins only.")
    @GetMapping
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAll();

    @Operation(summary = "Get Current User Profile", description = "Retrieves the profile of the currently authenticated user. Accessible by admins and users.")
    @GetMapping("/profile")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> getProfile();

    @Operation(summary = "Get User by ID", description = "Retrieves a specific user by their ID. Accessible by admins and the user themselves.")
    @GetMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> getById(
            @Parameter(description = "ID of the user to retrieve", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Update a User", description = "Updates an existing user by their ID with new details. Accessible by admins and the user themselves.")
    @PutMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> update(
            @Parameter(description = "ID of the user to update", required = true) @PathVariable Integer id,
            @Parameter(description = "Updated user details", required = true) @RequestBody UserDTO userDTO
    ) throws Exception;

    @Operation(summary = "Delete a User", description = "Deletes a user by their ID. Accessible by admins only.")
    @DeleteMapping("/{id}")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> delete(
            @Parameter(description = "ID of the user to delete", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Search Users", description = "Searches users based on name, email, and phone number. Accessible by admins only.")
    @GetMapping("/search")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> searchUsers(
            @Parameter(description = "Name of the user to filter by (optional)") @RequestParam(required = false) String name,
            @Parameter(description = "Email of the user to filter by (optional)") @RequestParam(required = false) String email,
            @Parameter(description = "Phone number of the user to filter by (optional)") @RequestParam(required = false) String phone
    );

    @Operation(summary = "Get All Users (Paged)", description = "Retrieves a paginated list of all users in the system. Accessible by admins only.")
    @PostMapping("/paged")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAllPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Search Users (Paged)", description = "Searches users with pagination based on name, email, and phone number. Accessible by admins only.")
    @PostMapping("/search/paged")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> searchUsersPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest,
            @Parameter(description = "Name of the user to filter by (optional)") @RequestParam(required = false) String name,
            @Parameter(description = "Email of the user to filter by (optional)") @RequestParam(required = false) String email,
            @Parameter(description = "Phone number of the user to filter by (optional)") @RequestParam(required = false) String phone
    );
}