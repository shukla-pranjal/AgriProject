package com.farmflow.endpoint;


import com.farmflow.dto.PaginationRequest;
import com.farmflow.enums.EntityType;
import com.farmflow.enums.FileType;
import com.farmflow.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Image Management", description = "Endpoints for managing images associated with entities")
@RequestMapping("/api/v1/images")
public interface ImageEndpoint {

    @Operation(summary = "Get All Images", description = "Retrieves a list of all images in the system. Accessible by admins only.")
    @GetMapping
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAll();

    @Operation(summary = "Get Image by ID", description = "Retrieves a specific image by its ID. Accessible by admins, users, and farmers.")
    @GetMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> getById(
            @Parameter(description = "ID of the image to retrieve", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Create a New Image", description = "Uploads a new image associated with an entity (e.g., user, product). Accessible by admins and farmers.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(Constants.ADMIN_OR_FARMER)
    ResponseEntity<?> create(
            @Parameter(description = "Image file to upload", required = true) @RequestParam MultipartFile file,
            @Parameter(description = "Type of entity the image is associated with (e.g., USER, PRODUCT)", required = true) @RequestParam EntityType entityType,
            @Parameter(description = "ID of the entity the image is associated with", required = true) @RequestParam Integer entityId
    ) throws Exception;

    @Operation(summary = "Update an Image", description = "Updates an existing image by its ID with a new file. Accessible by admins and farmers.")
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(Constants.ADMIN_OR_FARMER)
    ResponseEntity<?> update(
            @Parameter(description = "ID of the image to update", required = true) @PathVariable Integer id,
            @Parameter(description = "New image file to upload", required = true) @RequestParam MultipartFile file,
            @Parameter(description = "Type of entity the image is associated with (e.g., USER, PRODUCT)", required = true) @RequestParam EntityType entityType,
            @Parameter(description = "ID of the entity the image is associated with", required = true) @RequestParam Integer entityId
    ) throws Exception;

    @Operation(summary = "Delete an Image", description = "Deletes an image by its ID. Accessible by admins and farmers.")
    @DeleteMapping("/{id}")
    @PreAuthorize(Constants.ADMIN_OR_FARMER)
    ResponseEntity<?> delete(
            @Parameter(description = "ID of the image to delete", required = true) @PathVariable Integer id
    ) throws Exception;

    @Operation(summary = "Get Images by Entity", description = "Retrieves all images associated with a specific entity (e.g., user, product) by entity type and ID. Accessible by admins, users, and farmers.")
    @GetMapping("/entity/{entityType}/{entityId}")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> getByEntity(
            @Parameter(description = "Type of entity (e.g., USER, PRODUCT)", required = true) @PathVariable EntityType entityType,
            @Parameter(description = "ID of the entity", required = true) @PathVariable Integer entityId
    ) throws Exception;

    @Operation(summary = "Get Images by File Type", description = "Retrieves all images of a specific file type (e.g., JPEG, PNG). Accessible by admins only.")
    @GetMapping("/fileType/{fileType}")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getByFileType(
            @Parameter(description = "Type of file (e.g., JPEG, PNG)", required = true) @PathVariable FileType fileType
    ) throws Exception;

    @Operation(summary = "Search Images", description = "Searches images based on entity type, entity ID, file type, size range, and file name. Accessible by admins only.")
    @GetMapping("/search")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> searchImages(
            @Parameter(description = "Type of entity (e.g., USER, PRODUCT) to filter by (optional)") @RequestParam(required = false) EntityType entityType,
            @Parameter(description = "ID of the entity to filter by (optional)") @RequestParam(required = false) Integer entityId,
            @Parameter(description = "Type of file (e.g., JPEG, PNG) to filter by (optional)") @RequestParam(required = false) FileType fileType,
            @Parameter(description = "Minimum size of the image in bytes (optional)") @RequestParam(required = false) Long minSize,
            @Parameter(description = "Maximum size of the image in bytes (optional)") @RequestParam(required = false) Long maxSize,
            @Parameter(description = "Name of the file to filter by (optional)") @RequestParam(required = false) String fileName
    );

    @Operation(summary = "Get All Images (Paged)", description = "Retrieves a paginated list of all images in the system. Accessible by admins only.")
    @PostMapping("/paged")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> getAllPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    );

    @Operation(summary = "Get Images by Entity (Paged)", description = "Retrieves a paginated list of images associated with a specific entity by entity type and ID. Accessible by admins, users, and farmers.")
    @PostMapping("/entity/{entityType}/{entityId}/paged")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<?> getByEntityPaged(
            @Parameter(description = "Type of entity (e.g., USER, PRODUCT)", required = true) @PathVariable EntityType entityType,
            @Parameter(description = "ID of the entity", required = true) @PathVariable Integer entityId,
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest
    ) throws Exception;

    @Operation(summary = "Search Images (Paged)", description = "Searches images with pagination based on entity type, entity ID, file type, size range, and file name. Accessible by admins only.")
    @PostMapping("/search/paged")
    @PreAuthorize(Constants.ADMIN)
    ResponseEntity<?> searchImagesPaged(
            @Parameter(description = "Pagination details (page number, size, sort)", required = true) @RequestBody PaginationRequest paginationRequest,
            @Parameter(description = "Type of entity (e.g., USER, PRODUCT) to filter by (optional)") @RequestParam(required = false) EntityType entityType,
            @Parameter(description = "ID of the entity to filter by (optional)") @RequestParam(required = false) Integer entityId,
            @Parameter(description = "Type of file (e.g., JPEG, PNG) to filter by (optional)") @RequestParam(required = false) FileType fileType,
            @Parameter(description = "Minimum size of the image in bytes (optional)") @RequestParam(required = false) Long minSize,
            @Parameter(description = "Maximum size of the image in bytes (optional)") @RequestParam(required = false) Long maxSize,
            @Parameter(description = "Name of the file to filter by (optional)") @RequestParam(required = false) String fileName
    );

    @Operation(summary = "Download an Image", description = "Downloads a specific image by its ID as a byte array. Accessible by admins, users, and farmers.")
    @GetMapping("/{id}/download")
    @PreAuthorize(Constants.ADMIN_USER_FARMER)
    ResponseEntity<byte[]> downloadImage(
            @Parameter(description = "ID of the image to download", required = true) @PathVariable Integer id
    ) throws Exception;
}