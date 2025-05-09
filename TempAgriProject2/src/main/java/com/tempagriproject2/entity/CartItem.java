package com.tempagriproject2.entity;

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
public class CartItem extends BaseModel {
    @ManyToOne
    private Cart cart;
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;
    private Integer quantity;
}
