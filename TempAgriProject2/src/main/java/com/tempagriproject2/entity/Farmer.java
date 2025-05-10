package com.tempagriproject2.entity;

import jakarta.persistence.*;
import lombok.*;

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
}