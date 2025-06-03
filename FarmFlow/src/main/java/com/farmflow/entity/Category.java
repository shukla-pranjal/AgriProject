package com.farmflow.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Category extends BaseModel {
    private String name;
    private String description;


}
