package com.farmflow.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Response object for user details")
public class UserResponse {

    @Schema(description = "Unique identifier for the user", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer id;

    @Schema(description = "First name of the user", example = "John", requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3, maxLength = 50)
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String lastName;

    @Schema(description = "Email address of the user", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "^[A-Za-z0-9+_.-]+@(.+)$") // Adjust based on Constants.EMAIL_REGEX
    private String email;

    @Schema(description = "Password for the user account", example = "securePass123", requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 6, maxLength = 20)
    private String password;

    @Schema(description = "Mobile number of the user", example = "9876543210", requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "^\\d{10}$") // Adjust based on Constants.PHONE_REGEX
    private String mobno;

    @Schema(description = "Status of the user", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private StatusDto status;

    @ArraySchema(
            schema = @Schema(implementation = RoleDto.class, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    )
    @Schema(description = "List of roles assigned to the user", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<RoleDto> roles;

    // Nested RoleDto and StatusDto classes as defined above
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    @Schema(description = "Data Transfer Object for a user role")
    public static class RoleDto {
        @Schema(description = "Unique identifier for the role", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private Integer id;

        @Schema(description = "Name of the role", example = "ROLE_ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    @Schema(description = "Data Transfer Object for user status")
    public static class StatusDto {
        @Schema(description = "Unique identifier for the status", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private Integer id;

        @Schema(description = "Indicates if the user is active", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        private Boolean isActive;
    }
}