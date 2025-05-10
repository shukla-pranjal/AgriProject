package com.tempagriproject2.entity;

import com.tempagriproject2.enums.Unit;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Product extends BaseModel {
    private String name;
    private String description;
    @ManyToOne
    private Category category;
    private Double price;
    private Double quantity;
    @Enumerated(EnumType.STRING)
    private Unit unit;
    private LocalDateTime expiryDate;
    @ManyToOne
    private Farmer farmer;
    private Boolean available;
}
