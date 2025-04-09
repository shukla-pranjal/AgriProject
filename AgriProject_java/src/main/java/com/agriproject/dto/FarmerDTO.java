package com.agriproject.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmerDTO extends UserDTO {
    private String farmName;
    private String farmLocation;
    private double farmSizeInAcres;             // e.g., 15.5 acres
    private String farmType;                    // e.g., "Organic", "Mixed Farming", "Livestock", "Crop Farming"
    
    private List<CropDTO> crops;            // e.g., ["Wheat", "Tomatoes"]
    private boolean isCertifiedOrganic;         // for organic certification
    private String registrationNumber;          // some farms are officially registered
    private String experienceInYears;           // e.g., "10+ years"
    private String contactNumber;               // direct contact info
    private String governmentId;                // optional, e.g., Aadhar, SSN, etc.
}
