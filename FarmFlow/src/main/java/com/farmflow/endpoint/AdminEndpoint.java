package com.farmflow.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Admin Management", description = "Endpoints for managing admin roles and permissions")
@RequestMapping("/api/v1/admin")
public interface AdminEndpoint {

    @Operation(summary = "Promote User to Admin", description = "Promotes a user to admin role by their user ID. Accessible by admins only.")
    @PostMapping("/promote/{userId}")
    ResponseEntity<?> promote(
            @Parameter(description = "ID of the user to promote to admin", required = true) @PathVariable Integer userId
    ) throws Exception;

    @Operation(summary = "Demote User from Admin", description = "Demotes a user from admin role by their user ID. Accessible by admins only.")
    @PostMapping("/demote/{userId}")
    ResponseEntity<?> demote(
            @Parameter(description = "ID of the user to demote from admin", required = true) @PathVariable Integer userId
    ) throws Exception;
}