package com.farmflow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Review extends BaseModel {
    private Integer rating;
    private String comment;
    @ManyToOne
    private Product product;
    @ManyToOne
    private User user;
}
