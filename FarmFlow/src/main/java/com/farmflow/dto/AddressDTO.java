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
@Schema(description = "Data Transfer Object for address creation or update")
public class AddressDTO {

    @Schema(description = "Unique identifier for the address", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer id;

    @Schema(description = "Postal PIN code (must be a 6-digit number)", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "100000", maximum = "999999")
    private Integer pinCode;

    @Schema(description = "Street address", example = "123 Main Street", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String street;

    @Schema(description = "District of the address", example = "Mumbai", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 50)
    private String district;

    @Schema(description = "Type of address (e.g., Home, Work)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"1", "2", "3", "4", "5", "6"}) // Updated to 1-6
    private Integer addressType;

    @Schema(description = "State identifier (e.g., representing Indian states)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                    "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                    "31", "32", "33", "34", "35", "36"}) // Updated to 1-36
    private Integer state;

    @Schema(description = "ID of the user associated with this address", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer userId;
}