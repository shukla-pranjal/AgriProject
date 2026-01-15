package com.farmflow.endpoint;

import com.farmflow.dto.AddressDTO;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Address Management", description = "Endpoints for managing user addresses")
@RequestMapping("/api/v1/addresses")
public interface AddressEndpoint {

    @Operation(summary = "Get All Addresses", description = "Retrieves a list of all addresses in the system. Accessible by admins, users, and farmers.")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    @GetMapping
    ResponseEntity<?> getAll();

    @Operation(summary = "Get Address by ID", description = "Retrieves a specific address by its ID. Accessible by admins and the address owner.")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    @GetMapping("/{id}")
    ResponseEntity<?> getById(
            @Parameter(description = "ID of the address to retrieve", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Create a New Address", description = "Creates a new address for a user. Accessible by admins and users.")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    @PostMapping
    ResponseEntity<?> create(
            @Parameter(description = "Address details to create", required = true) @RequestBody AddressDTO addressDTO
    ) throws Exception;

    @Operation(summary = "Update an Address", description = "Updates an existing address by its ID. Accessible by admins and the address owner.")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    @PutMapping("/{id}")
    ResponseEntity<?> update(
            @Parameter(description = "ID of the address to update", required = true) @PathVariable Integer id,
            @Parameter(description = "Updated address details", required = true) @RequestBody AddressDTO addressDTO
    ) throws Exception;

    @Operation(summary = "Delete an Address", description = "Deletes an address by its ID. Accessible by admins and the address owner.")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(
            @Parameter(description = "ID of the address to delete", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Get Addresses by User ID", description = "Retrieves all addresses associated with a specific user ID. Accessible by admins and the user.")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    @GetMapping("/user/{userId}")
    ResponseEntity<?> getByUserId(
            @Parameter(description = "ID of the user whose addresses are to be retrieved", required = true) @PathVariable Integer userId
    ) throws Exception;

    @Operation(summary = "Search Addresses", description = "Searches addresses based on various criteria such as pin code, district, street, state, address type, and user ID. Accessible by admins, users, and farmers.")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    @GetMapping("/search")
    ResponseEntity<?> searchAddresses(
            @Parameter(description = "Pin code of the address") @RequestParam(required = false) Integer pinCode,
            @Parameter(description = "District of the address") @RequestParam(required = false) String district,
            @Parameter(description = "Street name of the address") @RequestParam(required = false) String street,
            @Parameter(description = "State ID of the address") @RequestParam(required = false) Integer state,
            @Parameter(description = "Type of the address (e.g., billing, shipping)") @RequestParam(required = false) Integer addressType,
            @Parameter(description = "ID of the user associated with the address") @RequestParam(required = false) Integer userId
    );

    @Operation(summary = "Get All Addresses (Paged)", description = "Retrieves a paginated list of all addresses in the system. Accessible by admins, users, and farmers.")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    @PostMapping("/paged")
    ResponseEntity<?> getAllPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Get Addresses by User ID (Paged)", description = "Retrieves a paginated list of addresses associated with a specific user ID. Accessible by admins and the user.")
    @PreAuthorize(Constants.ADMIN_OR_USER)
    @PostMapping("/user/{userId}/paged")
    ResponseEntity<?> getByUserIdPaged(
            @Parameter(description = "ID of the user whose addresses are to be retrieved", required = true) @PathVariable Integer userId,
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Search Addresses (Paged)", description = "Searches addresses with pagination based on criteria such as pin code, district, street, state, address type, and user ID. Accessible by admins, users, and farmers.")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    @PostMapping("/search/paged")
    ResponseEntity<?> searchAddressesPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest,
            @Parameter(description = "Pin code of the address") @RequestParam(required = false) Integer pinCode,
            @Parameter(description = "District of the address") @RequestParam(required = false) String district,
            @Parameter(description = "Street name of the address") @RequestParam(required = false) String street,
            @Parameter(description = "State ID of the address") @RequestParam(required = false) Integer state,
            @Parameter(description = "Type of the address (e.g., billing, shipping)") @RequestParam(required = false) Integer addressType,
            @Parameter(description = "ID of the user associated with the address") @RequestParam(required = false) Integer userId
    );

    @Operation(summary = "Get Addresses of Current User", description = "Retrieves all addresses of the currently authenticated user. Accessible by users and farmers.")
    @PreAuthorize(Constants.USER_OR_FARMER)
    @GetMapping("/user/me")
    ResponseEntity<?> getByCurrentUser() throws Exception;

    @Operation(summary = "Get Addresses of Current User (Paged)", description = "Retrieves a paginated list of addresses of the currently authenticated user. Accessible by users and farmers.")
    @PreAuthorize(Constants.USER_OR_FARMER)
    @PostMapping("/user/me/paged")
    ResponseEntity<?> getCurrentUserAddressesPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    ) ;

    @Operation(summary = "Search Addresses for Current User", description = "Searches addresses of the currently authenticated user based on criteria such as pin code, district, street, state, and address type. Accessible by users and farmers.")
    @PreAuthorize(Constants.USER_OR_FARMER)
    @GetMapping("/search/me")
    ResponseEntity<?> searchAddressesForCurrentUser(
            @Parameter(description = "Pin code of the address") @RequestParam(required = false) Integer pinCode,
            @Parameter(description = "District of the address") @RequestParam(required = false) String district,
            @Parameter(description = "Street name of the address") @RequestParam(required = false) String street,
            @Parameter(description = "State ID of the address") @RequestParam(required = false) Integer state,
            @Parameter(description = "Type of the address (e.g., billing, shipping)") @RequestParam(required = false) Integer addressType
    );

    @Operation(summary = "Search Addresses for Current User (Paged)", description = "Searches addresses of the currently authenticated user with pagination, based on criteria such as pin code, district, street, state, and address type. Accessible by users and farmers.")
    @PreAuthorize(Constants.USER_OR_FARMER)
    @PostMapping("/search/me/paged")
    ResponseEntity<?> searchAddressesForCurrentUserPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest,
            @Parameter(description = "Pin code of the address") @RequestParam(required = false) Integer pinCode,
            @Parameter(description = "District of the address") @RequestParam(required = false) String district,
            @Parameter(description = "Street name of the address") @RequestParam(required = false) String street,
            @Parameter(description = "State ID of the address") @RequestParam(required = false) Integer state,
            @Parameter(description = "Type of the address (e.g., billing, shipping)") @RequestParam(required = false) Integer addressType
    );
}