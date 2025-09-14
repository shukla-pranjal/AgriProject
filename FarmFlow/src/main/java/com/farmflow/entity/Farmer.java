package com.farmflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Farmer extends BaseModel {
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    private String farmName;
    private String farmType;
    private String locationDiscription;
    private String governmentId;
    /*// TODO think about this later
    private List<Crop> crops;
    private List<Livestock> livestock;
    private List<Equipment> equipmentOwned;
    */
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private List<Image> images = new ArrayList<>();
}