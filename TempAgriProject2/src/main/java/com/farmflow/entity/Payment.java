package com.farmflow.entity;

import com.farmflow.enums.PaymentMethod;
import com.farmflow.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Payment extends BaseModel {
    private String transactionId;
    private Double amount;
    @OneToOne
    private Orders orders;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

}
