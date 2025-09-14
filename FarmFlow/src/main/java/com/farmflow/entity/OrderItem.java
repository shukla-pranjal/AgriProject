package com.farmflow.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class OrderItem extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private Orders orders;

    private Integer productId;
    private String productName;
    private Double priceAtPurchase;
    private Integer quantity;
}