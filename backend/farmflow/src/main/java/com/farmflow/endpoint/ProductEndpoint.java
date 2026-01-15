package com.farmflow.endpoint;

import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.ProductDTO;
import com.farmflow.enums.Unit;
import com.farmflow.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Management", description = "Endpoints for managing products")
@RequestMapping("/api/v1/products")
public interface ProductEndpoint {

    @Operation(summary = "Get All Products", description = "Retrieves a list of all products in the system. Accessible by admins, users, and farmers.")
    @GetMapping
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> getAll();

    @Operation(summary = "Get All Available Products", description = "Retrieves a list of all available products (quantity > 0 and marked as available). Accessible by admins, users, and farmers.")
    @GetMapping("/available")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> getAllAvailable();

    @Operation(summary = "Get Product by ID", description = "Retrieves a specific product by its ID. Accessible by admins, users, and farmers.")
    @GetMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> getById(
            @Parameter(description = "ID of the product to retrieve", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Create a New Product", description = "Creates a new product with the provided details. Accessible by admins and farmers.")
    @PostMapping
    @PreAuthorize(Constants.ADMIN_OR_FARMER)
    ResponseEntity<?> create(
            @Parameter(description = "Product details to create", required = true) @RequestBody ProductDTO productDTO
    ) throws Exception;

    @Operation(summary = "Update a Product", description = "Updates an existing product by its ID with new details. Accessible by admins and farmers.")
    @PutMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_FARMER)
    ResponseEntity<?> update(
            @Parameter(description = "ID of the product to update", required = true) @PathVariable Integer id,
            @Parameter(description = "Updated product details", required = true) @RequestBody ProductDTO productDTO
    ) throws Exception;

    @Operation(summary = "Delete a Product", description = "Deletes a product by its ID. Accessible by admins and farmers.")
    @DeleteMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_FARMER)
    ResponseEntity<?> delete(
            @Parameter(description = "ID of the product to delete", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Get Products by Category ID", description = "Retrieves all products belonging to a specific category by category ID. Accessible by admins, users, and farmers.")
    @GetMapping("/category/{categoryId}")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> getByCategoryId(
            @Parameter(description = "ID of the category to retrieve products for", required = true) @PathVariable Integer categoryId
    ) throws Exception;

    @Operation(summary = "Get Products by Farmer ID", description = "Retrieves all products created by a specific farmer by farmer ID. Accessible by admins and the farmer.")
    @GetMapping("/farmer/{farmerId}")
    @PreAuthorize(Constants.ADMIN_OR_FARMER)
    ResponseEntity<?> getByFarmerId(
            @Parameter(description = "ID of the farmer to retrieve products for", required = true) @PathVariable Integer farmerId
    ) throws Exception;

    @Operation(summary = "Search Products", description = "Searches products based on name, category ID, farmer ID, availability, unit, and price range. Accessible by admins, users, and farmers.")
    @GetMapping("/search")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> searchProducts(
            @Parameter(description = "Name of the product to filter by (optional)") @RequestParam(required = false) String name,
            @Parameter(description = "ID of the category to filter by (optional)") @RequestParam(required = false) Integer categoryId,
            @Parameter(description = "ID of the farmer to filter by (optional)") @RequestParam(required = false) Integer farmerId,
            @Parameter(description = "Availability status to filter by (true/false, optional)") @RequestParam(required = false) Boolean available,
            @Parameter(description = "Unit of the product to filter by (optional)") @RequestParam(required = false) Unit unit,
            @Parameter(description = "Minimum price to filter by (optional)") @RequestParam(required = false) Double priceMin,
            @Parameter(description = "Maximum price to filter by (optional)") @RequestParam(required = false) Double priceMax
    );

    @Operation(summary = "Get All Products (Paged)", description = "Retrieves a paginated list of all products in the system. Accessible by admins, users, and farmers.")
    @PostMapping("/paged")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> getAllPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Get All Available Products (Paged)", description = "Retrieves a paginated list of all available products (quantity > 0 and marked as available). Accessible by admins, users, and farmers.")
    @PostMapping("/available/paged")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> getAllAvailablePaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Get Products by Category ID (Paged)", description = "Retrieves a paginated list of products belonging to a specific category by category ID. Accessible by admins, users, and farmers.")
    @PostMapping("/category/{categoryId}/paged")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> getByCategoryIdPaged(
            @Parameter(description = "ID of the category to retrieve products for", required = true) @PathVariable Integer categoryId,
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    ) throws Exception;

    @Operation(summary = "Get Products by Farmer ID (Paged)", description = "Retrieves a paginated list of products created by a specific farmer by farmer ID. Accessible by admins and the farmer.")
    @PostMapping("/farmer/{farmerId}/paged")
    @PreAuthorize(Constants.ADMIN_OR_FARMER)
    ResponseEntity<?> getByFarmerIdPaged(
            @Parameter(description = "ID of the farmer to retrieve products for", required = true) @PathVariable Integer farmerId,
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    ) throws Exception;

    @Operation(summary = "Search Products (Paged)", description = "Searches products with pagination based on name, category ID, farmer ID, availability, unit, and price range. Accessible by admins, users, and farmers.")
    @PostMapping("/search/paged")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> searchProductsPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest,
            @Parameter(description = "Name of the product to filter by (optional)") @RequestParam(required = false) String name,
            @Parameter(description = "ID of the category to filter by (optional)") @RequestParam(required = false) Integer categoryId,
            @Parameter(description = "ID of the farmer to filter by (optional)") @RequestParam(required = false) Integer farmerId,
            @Parameter(description = "Availability status to filter by (true/false, optional)") @RequestParam(required = false) Boolean available,
            @Parameter(description = "Unit of the product to filter by (optional)") @RequestParam(required = false) Unit unit,
            @Parameter(description = "Minimum price to filter by (optional)") @RequestParam(required = false) Double priceMin,
            @Parameter(description = "Maximum price to filter by (optional)") @RequestParam(required = false) Double priceMax
    );
}