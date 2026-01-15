package com.farmflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request DTO for an item in an order")
public class OrderItemDTO {

    @Schema(description = "Unique identifier for the product", example = "1", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1")
    private Integer productId;

    @Schema(description = "Name of the product", example = "Fresh Tomatoes", requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 2, maxLength = 100)
    private String productName;

    @Schema(description = "Price of the product at the time of purchase", example = "50.00", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0.01")
    private Double priceAtPurchase;

    @Schema(description = "Quantity of the product ordered", example = "2", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1")
    private Integer quantity;
}