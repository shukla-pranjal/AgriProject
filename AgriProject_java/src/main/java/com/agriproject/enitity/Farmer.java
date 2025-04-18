package com.agriproject.enitity;

import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
public class Farmer extends User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String farmName;
    private String farmLocation;
    private double farmSizeInAcres;
    private String farmType;
    
    @ManyToMany
    private List<Crop> crops;

    private boolean isCertifiedOrganic;
    private String registrationNumber;
    private String experienceInYears;
    private String contactNumber;
    private String governmentId;
}
