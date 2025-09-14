package com.farmflow.endpoint;

import com.farmflow.dto.CategoryDTO;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Category Management", description = "Endpoints for managing product categories")
@RequestMapping("/api/v1/categories")
public interface CategoryEndpoint {

    @Operation(summary = "Get All Categories", description = "Retrieves a list of all categories in the system. Accessible by admins, users, and farmers.")
    @GetMapping
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> getAll();

    @Operation(summary = "Get Category by ID", description = "Retrieves a specific category by its ID. Accessible by admins, users, and farmers.")
    @GetMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> getById(
            @Parameter(description = "ID of the category to retrieve", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Create a New Category", description = "Creates a new category. Accessible by admins only.")
    @PostMapping
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> create(
            @Parameter(description = "Category details to create", required = true) @RequestBody CategoryDTO categoryDTO
    ) throws Exception;

    @Operation(summary = "Update a Category", description = "Updates an existing category by its ID. Accessible by admins only.")
    @PutMapping("/{id}")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> update(
            @Parameter(description = "ID of the category to update", required = true) @PathVariable Integer id,
            @Parameter(description = "Updated category details", required = true) @RequestBody CategoryDTO categoryDTO
    ) throws Exception;

    @Operation(summary = "Delete a Category", description = "Deletes a category by its ID. Accessible by admins only.")
    @DeleteMapping("/{id}")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> delete(
            @Parameter(description = "ID of the category to delete", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Search Categories", description = "Searches categories based on name and description. Accessible by admins, users, and farmers.")
    @GetMapping("/search")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> searchCategories(
            @Parameter(description = "Name of the category to filter by (optional)") @RequestParam(required = false) String name,
            @Parameter(description = "Description of the category to filter by (optional)") @RequestParam(required = false) String description
    );

    @Operation(summary = "Get All Categories (Paged)", description = "Retrieves a paginated list of all categories in the system. Accessible by admins, users, and farmers.")
    @PostMapping("/paged")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> getAllCategoriesPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Search Categories (Paged)", description = "Searches categories with pagination based on name and description. Accessible by admins, users, and farmers.")
    @PostMapping("/search/paged")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> searchCategoriesPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest,
            @Parameter(description = "Name of the category to filter by (optional)") @RequestParam(required = false) String name,
            @Parameter(description = "Description of the category to filter by (optional)") @RequestParam(required = false) String description
    );
}