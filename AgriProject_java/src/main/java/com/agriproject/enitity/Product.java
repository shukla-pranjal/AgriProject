package com.agriproject.enitity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@EntityListeners(AuditingEntityListener.class)
public class  Product extends BaseModel{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;  
    private String name;
    private String description;
    private double price;
    @ManyToOne
    private Category category;
}