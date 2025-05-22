package com.farmflow.endpoint;

import com.farmflow.dto.CartDTO;
import com.farmflow.dto.CartItemDTO;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Tag(name = "Cart Management", description = "Endpoints for managing user carts")
@RequestMapping("/api/v1/carts")
public interface CartEndpoint {

    @Operation(summary = "Get All Carts", description = "Retrieves a list of all carts in the system. Accessible by admins only.")
    @GetMapping
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAll();

    @Operation(summary = "Get Cart by ID", description = "Retrieves a specific cart by its ID. Accessible by admins and the cart owner.")
    @GetMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> getById(
            @Parameter(description = "ID of the cart to retrieve", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Create a New Cart", description = "Creates a new cart for a user. Accessible by admins and users.")
    @PostMapping
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> create(
            @Parameter(description = "Cart details to create", required = true) @RequestBody CartDTO cartDTO
    ) throws Exception;

    @Operation(summary = "Update a Cart", description = "Updates an existing cart by its ID. Accessible by admins and the cart owner.")
    @PutMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> update(
            @Parameter(description = "ID of the cart to update", required = true) @PathVariable Integer id,
            @Parameter(description = "Updated cart details", required = true) @RequestBody CartDTO cartDTO
    ) throws Exception;

    @Operation(summary = "Delete a Cart", description = "Deletes a cart by its ID. Accessible by admins and the cart owner.")
    @DeleteMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> delete(
            @Parameter(description = "ID of the cart to delete", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Add Item to Cart", description = "Adds an item to a specific cart by its ID. Accessible by admins and the cart owner.")
    @PostMapping("/{id}/items")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> addItem(
            @Parameter(description = "ID of the cart to add the item to", required = true) @PathVariable Integer id,
            @Parameter(description = "Details of the cart item to add", required = true) @RequestBody CartItemDTO cartItemDTO
    ) throws Exception;

    @Operation(summary = "Update Item Quantity in Cart", description = "Updates the quantity of a specific item in a cart by cart ID and product ID. Accessible by admins and the cart owner.")
    @PutMapping("/{id}/items/{productId}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> updateItemQuantity(
            @Parameter(description = "ID of the cart containing the item", required = true) @PathVariable Integer id,
            @Parameter(description = "ID of the product to update", required = true) @PathVariable Integer productId,
            @Parameter(description = "Updated cart item details (e.g., new quantity)", required = true) @RequestBody CartItemDTO cartItemDTO
    ) throws Exception;

    @Operation(summary = "Remove Item from Cart", description = "Removes a specific item from a cart by cart ID and product ID. Accessible by admins and the cart owner.")
    @DeleteMapping("/{id}/items/{productId}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> removeItem(
            @Parameter(description = "ID of the cart to remove the item from", required = true) @PathVariable Integer id,
            @Parameter(description = "ID of the product to remove", required = true) @PathVariable Integer productId
    ) throws Exception;

    @Operation(summary = "Increase Item Quantity in Cart", description = "Increases the quantity of a specific item in a cart by a specified amount (default is 1). Accessible by admins and the cart owner.")
    @PutMapping("/{id}/items/{productId}/increase")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> increaseItemQuantity(
            @Parameter(description = "ID of the cart containing the item", required = true) @PathVariable Integer id,
            @Parameter(description = "ID of the product to increase quantity for", required = true) @PathVariable Integer productId,
            @Parameter(description = "Amount to increase the quantity by (default is 1)") @RequestParam(defaultValue = "1") Integer amount
    ) throws Exception;

    @Operation(summary = "Decrease Item Quantity in Cart", description = "Decreases the quantity of a specific item in a cart by a specified amount (default is 1). Accessible by admins and the cart owner.")
    @PutMapping("/{id}/items/{productId}/decrease")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> decreaseItemQuantity(
            @Parameter(description = "ID of the cart containing the item", required = true) @PathVariable Integer id,
            @Parameter(description = "ID of the product to decrease quantity for", required = true) @PathVariable Integer productId,
            @Parameter(description = "Amount to decrease the quantity by (default is 1)") @RequestParam(defaultValue = "1") Integer amount
    ) throws Exception;

    @Operation(summary = "Search Items in Cart", description = "Searches for items in a specific cart by cart ID, with optional filters for product name and minimum quantity. Accessible by admins and the cart owner.")
    @GetMapping("/{id}/items/search")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> searchItemsInCart(
            @Parameter(description = "ID of the cart to search items in", required = true) @PathVariable Integer id,
            @Parameter(description = "Name of the product to filter by (optional)") @RequestParam(required = false) String productName,
            @Parameter(description = "Minimum quantity of the product to filter by (optional)") @RequestParam(required = false) Integer minQuantity
    ) throws Exception;

    @Operation(summary = "Get All Carts (Paged)", description = "Retrieves a paginated list of all carts in the system. Accessible by admins only.")
    @PostMapping("/paged")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAllCartsPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Search Items in Cart (Paged)", description = "Searches for items in a specific cart with pagination, by cart ID, with optional filters for product name and minimum quantity. Accessible by admins and the cart owner.")
    @PostMapping("/{id}/items/paged")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> searchItemsInCartPaged(
            @Parameter(description = "ID of the cart to search items in", required = true) @PathVariable Integer id,
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest,
            @Parameter(description = "Name of the product to filter by (optional)") @RequestParam(required = false) String productName,
            @Parameter(description = "Minimum quantity of the product to filter by (optional)") @RequestParam(required = false) Integer minQuantity
    ) throws Exception;
}