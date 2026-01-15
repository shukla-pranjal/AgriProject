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
@Schema(description = "Data Transfer Object for a single item in the cart")
public class CartItemDTO {

    @Schema(description = "ID of the product in the cart", example = "1", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1") // Must be greater than 0
    private Integer productId;

    @Schema(description = "Quantity of the product in the cart", example = "2", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1") // Must be greater than 0
    private Integer quantity;
}