package com.farmflow.dto;

import com.farmflow.enums.PaymentMethod;
import com.farmflow.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Data Transfer Object for payment details")
public class PaymentDTO {

    @Schema(description = "Unique identifier for the payment", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private int id;

    @Schema(description = "Transaction ID for the payment", example = "TXN123456789", requiredMode = Schema.RequiredMode.REQUIRED)
    private String transactionId;

    @Schema(description = "Amount of the payment", example = "1500.50", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0.01") // Must be greater than 0
    private Double amount;

    @Schema(description = "ID of the order associated with the payment", example = "1", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1")
    private Integer ordersId;

    @Schema(description = "Status of the payment", example = "COMPLETED", requiredMode = Schema.RequiredMode.REQUIRED)
    private PaymentStatus paymentStatus;

    @Schema(description = "Method used for the payment", example = "CARD", requiredMode = Schema.RequiredMode.REQUIRED)
    private PaymentMethod paymentMethod;
}