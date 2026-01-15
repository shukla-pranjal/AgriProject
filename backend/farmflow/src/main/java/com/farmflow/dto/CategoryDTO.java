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
@Schema(description = "Data Transfer Object for a product category")
public class CategoryDTO {

    @Schema(description = "Unique identifier for the category", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer id;

    @Schema(description = "Name of the category", example = "Vegetables", requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3, maxLength = 50)
    private String name;

    @Schema(description = "Description of the category", example = "Fresh vegetables", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;
}