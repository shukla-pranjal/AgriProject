package com.tempagriproject2.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class CartItem extends BaseModel {
    private String name;
    private String description;
}
