package com.farmflow.entity;

import com.farmflow.enums.Unit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private List<Image> images = new ArrayList<>();
}
