package com.farmflow.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

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


    List<ImageDTO> images = new ArrayList<>();
    // Getters and setters
}