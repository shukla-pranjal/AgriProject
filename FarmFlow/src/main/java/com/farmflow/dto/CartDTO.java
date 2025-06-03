package com.farmflow.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "Data Transfer Object for a user's shopping cart")
public class CartDTO {

    @Schema(description = "Unique identifier for the cart", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private int id;

    @Schema(description = "ID of the user associated with the cart", example = "1", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1") // Assumed required and must be greater than 0
    private Integer userId;

    @ArraySchema(
            schema = @Schema(implementation = CartItemDTO.class, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    )
    @Schema(description = "List of items in the cart", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<CartItemDTO> items;
}