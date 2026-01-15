package com.farmflow.endpoint;

import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.ReviewDTO;
import com.farmflow.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review Management", description = "Endpoints for managing product reviews")
@RequestMapping("/api/v1/reviews")
public interface ReviewEndpoint {

    @Operation(summary = "Get All Reviews", description = "Retrieves a list of all reviews in the system. Accessible by admins only.")
    @GetMapping
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAll();

    @Operation(summary = "Get Review by ID", description = "Retrieves a specific review by its ID. Accessible by admins and the review owner.")
    @GetMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> getById(
            @Parameter(description = "ID of the review to retrieve", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Create a New Review", description = "Creates a new review for a product. Accessible by admins and users.")
    @PostMapping
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> create(
            @Parameter(description = "Review details to create", required = true) @RequestBody ReviewDTO reviewDTO
    ) throws Exception;

    @Operation(summary = "Update a Review", description = "Updates an existing review by its ID. Accessible by admins and the review owner.")
    @PutMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> update(
            @Parameter(description = "ID of the review to update", required = true) @PathVariable Integer id,
            @Parameter(description = "Updated review details", required = true) @RequestBody ReviewDTO reviewDTO
    ) throws Exception;

    @Operation(summary = "Delete a Review", description = "Deletes a review by its ID. Accessible by admins and the review owner.")
    @DeleteMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> delete(
            @Parameter(description = "ID of the review to delete", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Get Reviews by Product ID", description = "Retrieves all reviews for a specific product by product ID. Accessible by admins and farmers.")
    @GetMapping("/product/{productId}")
    @PreAuthorize(Constants.ADMIN_OR_FARMER)
    ResponseEntity<?> getByProductId(
            @Parameter(description = "ID of the product to retrieve reviews for", required = true) @PathVariable Integer productId
    ) throws Exception;

    @Operation(summary = "Get Reviews by User ID", description = "Retrieves all reviews submitted by a specific user by user ID. Accessible by admins and the user.")
    @GetMapping("/user/{userId}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> getByUserId(
            @Parameter(description = "ID of the user whose reviews are to be retrieved", required = true) @PathVariable Integer userId
    ) throws Exception;

    @Operation(summary = "Search Reviews", description = "Searches reviews based on product ID, user ID, rating range, and comment content. Accessible by admins and users.")
    @GetMapping("/search")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> searchReviews(
            @Parameter(description = "ID of the product to filter by (optional)") @RequestParam(required = false) Integer productId,
            @Parameter(description = "ID of the user to filter by (optional)") @RequestParam(required = false) Integer userId,
            @Parameter(description = "Minimum rating to filter by (optional)") @RequestParam(required = false) Integer minRating,
            @Parameter(description = "Maximum rating to filter by (optional)") @RequestParam(required = false) Integer maxRating,
            @Parameter(description = "Comment content to filter by (partial match, optional)") @RequestParam(required = false) String comment
    );

    @Operation(summary = "Get All Reviews (Paged)", description = "Retrieves a paginated list of all reviews in the system. Accessible by admins only.")
    @PostMapping("/paged")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAllPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Get Reviews by Product ID (Paged)", description = "Retrieves a paginated list of reviews for a specific product by product ID. Accessible by admins and farmers.")
    @PostMapping("/product/{productId}/paged")
    @PreAuthorize(Constants.ADMIN_OR_FARMER)
    ResponseEntity<?> getByProductIdPaged(
            @Parameter(description = "ID of the product to retrieve reviews for", required = true) @PathVariable Integer productId,
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    ) throws Exception;

    @Operation(summary = "Get Reviews by User ID (Paged)", description = "Retrieves a paginated list of reviews submitted by a specific user by user ID. Accessible by admins and the user.")
    @PostMapping("/user/{userId}/paged")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> getByUserIdPaged(
            @Parameter(description = "ID of the user whose reviews are to be retrieved", required = true) @PathVariable Integer userId,
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    ) throws Exception;

    @Operation(summary = "Search Reviews (Paged)", description = "Searches reviews with pagination based on product ID, user ID, rating range, and comment content. Accessible by admins and users.")
    @PostMapping("/search/paged")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> searchReviewsPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest,
            @Parameter(description = "ID of the product to filter by (optional)") @RequestParam(required = false) Integer productId,
            @Parameter(description = "ID of the user to filter by (optional)") @RequestParam(required = false) Integer userId,
            @Parameter(description = "Minimum rating to filter by (optional)") @RequestParam(required = false) Integer minRating,
            @Parameter(description = "Maximum rating to filter by (optional)") @RequestParam(required = false) Integer maxRating,
            @Parameter(description = "Comment content to filter by (partial match, optional)") @RequestParam(required = false) String comment
    );
}