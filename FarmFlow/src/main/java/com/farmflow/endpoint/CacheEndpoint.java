package com.farmflow.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RequestMapping("/api/v1/cache")
@Tag(name = "Cache Management", description = "Endpoints for managing application caches")
@PreAuthorize("hasRole('ADMIN')")
public interface CacheEndpoint {

    @Operation(summary = "Clear a Specific Cache", description = "Clears a specified cache (e.g., orderCache, paymentCache). Accessible only by admins.")
    @DeleteMapping("/{cacheName}")
    ResponseEntity<?> clearCache(
            @Parameter(description = "Name of the cache to clear (e.g., orderCache, reviewCache)", required = true) @PathVariable String cacheName
    );

    @Operation(summary = "Clear All Caches", description = "Clears all caches in the application. Accessible only by admins.")
    @DeleteMapping("/all")
    ResponseEntity<?> clearAllCaches() ;

    @Operation(summary = "Check Cache Status", description = "Checks if a specified cache is empty. Accessible only by admins.")
    @GetMapping("/status/{cacheName}")
    ResponseEntity<?> isCacheEmpty(
            @Parameter(description = "Name of the cache to check (e.g., orderCache, reviewCache)", required = true) @PathVariable String cacheName
    );

    @Operation(summary = "Get Cache Contents", description = "Retrieves the contents of a specified cache. Accessible only by admins.")
    @GetMapping("/{cacheName}")
    ResponseEntity<?> getCache(
            @Parameter(description = "Name of the cache to retrieve (e.g., orderCache, reviewCache)", required = true) @PathVariable String cacheName
    ) ;

    @Operation(summary = "Get All Cache Names", description = "Retrieves a list of all cache names in the application. Accessible only by admins.")
    @GetMapping("/all")
    ResponseEntity<?> getAllCaches();
}