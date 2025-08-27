package com.farmflow.dto;

import com.farmflow.enums.OrdersStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request DTO for creating or updating an order")
public class OrdersDTO {

    @Schema(description = "Unique identifier for the order (optional for creation, required for update)", example = "1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer id;

    @Schema(description = "ID of the user who placed the order", example = "101", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1")
    private Integer userId;

    @Schema(description = "Date and time when the order was placed (auto-generated if not provided)", example = "2025-06-23T00:31:00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime orderDate;

    @Schema(description = "Status of the order", example = "PENDING", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"PENDING", "PAID", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"})
    private OrdersStatus status;

    @Schema(description = "Total price of the order (auto-calculated if not provided)", example = "150.50",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, minimum = "0.01")
    private Double totalPrice;

    @Schema(description = "List of items in the order", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<OrderItemDTO> items;
}