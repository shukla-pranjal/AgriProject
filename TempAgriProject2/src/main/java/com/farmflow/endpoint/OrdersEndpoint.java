package com.farmflow.endpoint;

import com.farmflow.dto.OrdersDTO;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.enums.OrdersStatus;
import com.farmflow.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Order Management", description = "Endpoints for managing orders")
@RequestMapping("/api/v1/orders")
public interface OrdersEndpoint {

    @Operation(summary = "Place Order from Cart", description = "Places a new order using the items in a specific cart by cart ID. Accessible by admins and users.")
    @PostMapping("/cart/{cartId}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> placeOrderFromCart(
            @Parameter(description = "ID of the cart to place the order from", required = true) @PathVariable Integer cartId
    ) throws Exception;

    @Operation(summary = "Create a New Order", description = "Creates a new order with the provided details. Accessible by admins and users.")
    @PostMapping
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> createOrder(
            @Parameter(description = "Order details to create", required = true) @RequestBody OrdersDTO ordersDTO
    ) throws Exception;

    @Operation(summary = "Reorder a Previous Order", description = "Reorders a previous order by its ID. Accessible by admins and users.")
    @PostMapping("/{id}/reorder")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> reorder(
            @Parameter(description = "ID of the order to reorder", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Cancel an Order", description = "Cancels an existing order by its ID. Accessible by admins and users.")
    @PutMapping("/{id}/cancel")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> cancelOrder(
            @Parameter(description = "ID of the order to cancel", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Update Order Status", description = "Updates the status of an existing order by its ID. Accessible by admins only.")
    @PutMapping("/{id}/status")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> updateOrderStatus(
            @Parameter(description = "ID of the order to update", required = true) @PathVariable Integer id,
            @Parameter(description = "New status of the order (e.g., PENDING, PAID)", required = true) @RequestParam OrdersStatus status
    ) throws Exception;

    @Operation(summary = "Get Order by ID", description = "Retrieves a specific order by its ID. Accessible by admins and users.")
    @GetMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> getOrderById(
            @Parameter(description = "ID of the order to retrieve", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Get Orders by User ID", description = "Retrieves all orders associated with a specific user ID. Accessible by admins and the user.")
    @GetMapping("/user/{userId}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> getOrdersByUser(
            @Parameter(description = "ID of the user whose orders are to be retrieved", required = true) @PathVariable Integer userId
    ) throws Exception;

    @Operation(summary = "Get All Orders", description = "Retrieves a list of all orders in the system. Accessible by admins only.")
    @GetMapping
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAllOrders();

    @Operation(summary = "Delete an Order", description = "Deletes an order by its ID. Accessible by admins only.")
    @DeleteMapping("/{id}")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> deleteOrder(
            @Parameter(description = "ID of the order to delete", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Search Orders", description = "Searches orders based on user ID, date range, status, and total price range. Accessible by admins and users.")
    @GetMapping("/search")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> searchOrders(
            @Parameter(description = "ID of the user to filter by (optional)") @RequestParam(required = false) Integer userId,
            @Parameter(description = "Start date and time to filter by (ISO format, optional)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @Parameter(description = "End date and time to filter by (ISO format, optional)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @Parameter(description = "Status of the order to filter by (e.g., PENDING, PAID, optional)") @RequestParam(required = false) OrdersStatus status,
            @Parameter(description = "Minimum total price to filter by (optional)") @RequestParam(required = false) Double minTotalPrice,
            @Parameter(description = "Maximum total price to filter by (optional)") @RequestParam(required = false) Double maxTotalPrice
    );

    @Operation(summary = "Get All Orders (Paged)", description = "Retrieves a paginated list of all orders in the system. Accessible by admins only.")
    @PostMapping("/paged")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAllOrdersPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Get Orders by User ID (Paged)", description = "Retrieves a paginated list of orders associated with a specific user ID. Accessible by admins and the user.")
    @PostMapping("/user/{userId}/paged")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> getOrdersByUserPaged(
            @Parameter(description = "ID of the user whose orders are to be retrieved", required = true) @PathVariable Integer userId,
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    ) throws Exception;

    @Operation(summary = "Search Orders (Paged)", description = "Searches orders with pagination based on user ID, date range, status, and total price range. Accessible by admins and users.")
    @PostMapping("/search/paged")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> searchOrdersPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest,
            @Parameter(description = "ID of the user to filter by (optional)") @RequestParam(required = false) Integer userId,
            @Parameter(description = "Start date and time to filter by (ISO format, optional)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @Parameter(description = "End date and time to filter by (ISO format, optional)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @Parameter(description = "Status of the order to filter by (e.g., PENDING, PAID, optional)") @RequestParam(required = false) OrdersStatus status,
            @Parameter(description = "Minimum total price to filter by (optional)") @RequestParam(required = false) Double minTotalPrice,
            @Parameter(description = "Maximum total price to filter by (optional)") @RequestParam(required = false) Double maxTotalPrice
    );
}