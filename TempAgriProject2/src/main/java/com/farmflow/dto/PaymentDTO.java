package com.farmflow.dto;

import com.farmflow.enums.PaymentMethod;
import com.farmflow.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {
    private int id;
    private String transactionId;
    private Double amount;
    private Integer ordersId;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
}
