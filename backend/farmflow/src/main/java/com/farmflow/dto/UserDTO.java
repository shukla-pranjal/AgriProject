package com.farmflow.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "Data Transfer Object for user creation or update")
public class UserDTO {

    @Schema(description = "Unique identifier for the user", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private int id;

    @Schema(description = "Name of the user", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3, maxLength = 50)
    private String name;

    @Schema(description = "Email address of the user", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "^[A-Za-z0-9+_.-]+@(.+)$") // Adjust based on Constants.EMAIL_REGEX
    private String email;

    @Schema(description = "Password for the user account", example = "securePass123", requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 6, maxLength = 20)
    private String password;

    @Schema(description = "Phone number of the user", example = "9876543210", requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "^\\d{10}$") // Adjust based on Constants.PHONE_REGEX
    private String phone;

    @Schema(description = "Image associated with the user", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ImageDTO image;
}