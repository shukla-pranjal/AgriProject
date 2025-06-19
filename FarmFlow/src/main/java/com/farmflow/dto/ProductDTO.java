package com.farmflow.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "Data Transfer Object for a product")
public class ProductDTO {

    @Schema(description = "Unique identifier for the product", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private int id;

    @Schema(description = "Name of the product", example = "Fresh Tomatoes", requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 2, maxLength = 100)
    private String name;

    @Schema(description = "Description of the product", example = "Organic tomatoes grown locally", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 300)
    private String description;

    @Schema(description = "ID of the category the product belongs to", example = "1", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1")
    private Integer categoryId;

    @Schema(description = "Price of the product", example = "50.00", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0.01") // Must be greater than 0
    private Double price;

    @Schema(description = "Quantity of the product", example = "10.5", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0.01") // Must be greater than 0
    private Double quantity;

    @Schema(description = "Unit of measurement for the product quantity", example = "1", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"1", "2", "3", "4", "5"}) // Placeholder for Unit enum (e.g., 1=KG, 2=LITER)
    private Integer unit;

    @Schema(description = "Expiry date of the product (must be in the future if provided)", example = "2025-12-31T23:59:59",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime expiryDate;

    @Schema(description = "ID of the farmer who owns the product", example = "1", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1")
    private Integer farmerId;

    @Schema(description = "Availability status of the product", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean available;

    @ArraySchema(
            schema = @Schema(implementation = ImageDTO.class, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    )
    @Schema(description = "List of images associated with the product", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<ImageDTO> images = new ArrayList<>();
}