package com.agriproject.enitity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Cart extends BaseModel{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;  
    private String name;
    private String description;  
}
