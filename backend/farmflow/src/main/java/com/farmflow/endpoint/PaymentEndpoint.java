package com.farmflow.endpoint;

import com.farmflow.dto.PaginationRequest;
import com.farmflow.dto.PaymentDTO;
import com.farmflow.enums.PaymentMethod;
import com.farmflow.enums.PaymentStatus;
import com.farmflow.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Payment Management", description = "Endpoints for managing payments")
@RequestMapping("/api/v1/payments")
public interface PaymentEndpoint {

    @Operation(summary = "Create a New Payment", description = "Creates a new payment with the provided details. Accessible by admins and users.")
    @PostMapping
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> createPayment(
            @Parameter(description = "Payment details to create", required = true) @RequestBody PaymentDTO paymentDTO
    ) throws Exception;

    @Operation(summary = "Get Payment by ID", description = "Retrieves a specific payment by its ID. Accessible by admins and users.")
    @GetMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> getPaymentById(
            @Parameter(description = "ID of the payment to retrieve", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Get All Payments", description = "Retrieves a list of all payments in the system. Accessible by admins only.")
    @GetMapping
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAllPayments();

    @Operation(summary = "Get Payment by Order ID", description = "Retrieves the payment associated with a specific order ID. Accessible by admins and users.")
    @GetMapping("/order/{orderId}")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    ResponseEntity<?> getPaymentByOrderId(
            @Parameter(description = "ID of the order to retrieve the payment for", required = true) @PathVariable Integer orderId
    ) throws Exception;

    @Operation(summary = "Get Payments by Status", description = "Retrieves all payments with a specific status (e.g., COMPLETED, PENDING). Accessible by admins only.")
    @GetMapping("/status")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getPaymentsByStatus(
            @Parameter(description = "Status of the payments to filter by (e.g., COMPLETED, PENDING)", required = true) @RequestParam PaymentStatus status
    ) throws Exception;

    @Operation(summary = "Get Payments by Method", description = "Retrieves all payments made using a specific payment method (e.g., CREDIT_CARD, PAYPAL). Accessible by admins only.")
    @GetMapping("/method")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getPaymentsByMethod(
            @Parameter(description = "Payment method to filter by (e.g., CREDIT_CARD, PAYPAL)", required = true) @RequestParam PaymentMethod method
    ) throws Exception;

    @Operation(summary = "Update a Payment", description = "Updates an existing payment by its ID with new details. Accessible by admins only.")
    @PutMapping("/{id}")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> updatePayment(
            @Parameter(description = "ID of the payment to update", required = true) @PathVariable Integer id,
            @Parameter(description = "Updated payment details", required = true) @RequestBody PaymentDTO paymentDTO
    ) throws Exception;

    @Operation(summary = "Update Payment Status", description = "Updates the status of an existing payment by its ID. Accessible by admins only.")
    @PatchMapping("/{id}/status")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> updatePaymentStatus(
            @Parameter(description = "ID of the payment to update", required = true) @PathVariable Integer id,
            @Parameter(description = "New status of the payment (e.g., COMPLETED, PENDING)", required = true) @RequestParam PaymentStatus status
    ) throws Exception;

    @Operation(summary = "Update Payment Method", description = "Updates the payment method of an existing payment by its ID. Accessible by admins only.")
    @PatchMapping("/{id}/method")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> updatePaymentMethod(
            @Parameter(description = "ID of the payment to update", required = true) @PathVariable Integer id,
            @Parameter(description = "New payment method (e.g., CREDIT_CARD, PAYPAL)", required = true) @RequestParam PaymentMethod method
    ) throws Exception;

    @Operation(summary = "Delete a Payment", description = "Deletes a payment by its ID. Accessible by admins only.")
    @DeleteMapping("/{id}")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> deletePayment(
            @Parameter(description = "ID of the payment to delete", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Search Payments", description = "Searches payments based on status, payment method, and amount range. Accessible by admins only.")
    @GetMapping("/search")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> searchPayments(
            @Parameter(description = "Status of the payments to filter by (e.g., COMPLETED, PENDING, optional)") @RequestParam(required = false) PaymentStatus status,
            @Parameter(description = "Payment method to filter by (e.g., CREDIT_CARD, PAYPAL, optional)") @RequestParam(required = false) PaymentMethod method,
            @Parameter(description = "Minimum amount to filter by (optional)") @RequestParam(required = false) Double minAmount,
            @Parameter(description = "Maximum amount to filter by (optional)") @RequestParam(required = false) Double maxAmount
    );

    @Operation(summary = "Get All Payments (Paged)", description = "Retrieves a paginated list of all payments in the system. Accessible by admins only.")
    @PostMapping("/paged")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAllPaymentsPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Get Payments by Status (Paged)", description = "Retrieves a paginated list of payments with a specific status (e.g., COMPLETED, PENDING). Accessible by admins only.")
    @PostMapping("/status/paged")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getPaymentsByStatusPaged(
            @Parameter(description = "Status of the payments to filter by (e.g., COMPLETED, PENDING)", required = true) @RequestParam PaymentStatus status,
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Get Payments by Method (Paged)", description = "Retrieves a paginated list of payments made using a specific payment method (e.g., CREDIT_CARD, PAYPAL). Accessible by admins only.")
    @PostMapping("/method/paged")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getPaymentsByMethodPaged(
            @Parameter(description = "Payment method to filter by (e.g., CREDIT_CARD, PAYPAL)", required = true) @RequestParam PaymentMethod method,
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Search Payments (Paged)", description = "Searches payments with pagination based on status, payment method, and amount range. Accessible by admins only.")
    @PostMapping("/search/paged")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> searchPaymentsPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest,
            @Parameter(description = "Status of the payments to filter by (e.g., COMPLETED, PENDING, optional)") @RequestParam(required = false) PaymentStatus status,
            @Parameter(description = "Payment method to filter by (e.g., CREDIT_CARD, PAYPAL, optional)") @RequestParam(required = false) PaymentMethod method,
            @Parameter(description = "Minimum amount to filter by (optional)") @RequestParam(required = false) Double minAmount,
            @Parameter(description = "Maximum amount to filter by (optional)") @RequestParam(required = false) Double maxAmount
    );
}