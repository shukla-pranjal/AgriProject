package com.tempagriproject2.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FarmerDTO {
    private int id;
    private Integer userId;
    private String farmName;
    private String farmType;
    private String locationDiscription;
    private String governmentId;

    // Getters and setters
}