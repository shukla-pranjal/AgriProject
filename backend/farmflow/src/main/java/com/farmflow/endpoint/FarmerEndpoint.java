package com.farmflow.endpoint;

import com.farmflow.dto.FarmerDTO;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Farmer Management", description = "Endpoints for managing farmers")
@RequestMapping("/api/v1/farmers")
public interface FarmerEndpoint {

    @Operation(summary = "Get All Farmers", description = "Retrieves a list of all farmers in the system. Accessible by admins only.")
    @GetMapping
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAll();

    @Operation(summary = "Get Farmer by ID", description = "Retrieves a specific farmer by their ID. Accessible by admins and the farmer themselves.")
    @GetMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_FARMER)
    ResponseEntity<?> getById(
            @Parameter(description = "ID of the farmer to retrieve", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Create a New Farmer", description = "Creates a new farmer profile. Accessible by admins and users.")
    @PostMapping
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> create(
            @Parameter(description = "Farmer details to create", required = true) @RequestBody FarmerDTO farmerDTO
    ) throws Exception;

    @Operation(summary = "Update a Farmer", description = "Updates an existing farmer by their ID. Accessible by admins and the farmer themselves.")
    @PutMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_FARMER)
    ResponseEntity<?> update(
            @Parameter(description = "ID of the farmer to update", required = true) @PathVariable Integer id,
            @Parameter(description = "Updated farmer details", required = true) @RequestBody FarmerDTO farmerDTO
    ) throws Exception;

    @Operation(summary = "Delete a Farmer", description = "Deletes a farmer by their ID. Accessible by admins and the farmer themselves.")
    @DeleteMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_FARMER)
    ResponseEntity<?> delete(
            @Parameter(description = "ID of the farmer to delete", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Check if User is a Farmer", description = "Checks if a user with the specified ID is a farmer. Accessible by admins and users.")
    @GetMapping("/isFarmer/{userId}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> isUserFarmer(
            @Parameter(description = "ID of the user to check for farmer status", required = true) @PathVariable Integer userId
    ) throws Exception;

    @Operation(summary = "Search Farmers", description = "Searches farmers based on farm name, farm type, location description, and government ID. Accessible by admins only.")
    @GetMapping("/search")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> searchFarmers(
            @Parameter(description = "Name of the farm to filter by (optional)") @RequestParam(required = false) String farmName,
            @Parameter(description = "Type of the farm to filter by (optional)") @RequestParam(required = false) String farmType,
            @Parameter(description = "Location description of the farm to filter by (optional)") @RequestParam(required = false) String locationDiscription,
            @Parameter(description = "Government ID of the farmer to filter by (optional)") @RequestParam(required = false) String governmentId
    );

    @Operation(summary = "Get All Farmers (Paged)", description = "Retrieves a paginated list of all farmers in the system. Accessible by admins only.")
    @PostMapping("/paged")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAllFarmersPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Search Farmers (Paged)", description = "Searches farmers with pagination based on farm name, farm type, location description, and government ID. Accessible by admins only.")
    @PostMapping("/search/paged")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> searchFarmersPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest,
            @Parameter(description = "Name of the farm to filter by (optional)") @RequestParam(required = false) String farmName,
            @Parameter(description = "Type of the farm to filter by (optional)") @RequestParam(required = false) String farmType,
            @Parameter(description = "Location description of the farm to filter by (optional)") @RequestParam(required = false) String locationDiscription,
            @Parameter(description = "Government ID of the farmer to filter by (optional)") @RequestParam(required = false) String governmentId
    );
}