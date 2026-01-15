package com.farmflow.endpoint;

import com.farmflow.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Management", description = "Endpoints for managing admin roles and permissions")
@RequestMapping("/api/v1/admin")
@PreAuthorize(Constants.ADMIN)
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

    @Operation(summary = "Toggle Email Service", description = "Toggles the email service on or off. Accessible by admins only.")
    @PostMapping("/email-service/toggle")
    ResponseEntity<?> toggleEmailService();

    @Operation(summary = "Get Email Service Status", description = "Checks the current status of the email service. Accessible by admins only.")
    @GetMapping("/email-service/status")
    ResponseEntity<?> getEmailServiceStatus();


}
