package com.farmflow.dto.ml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ML7Dto {
    private Integer id;
    private int temparature;
    private int humidity;
    private int moisture;
    private int nitrogen;
    private int potassium;
    private int phosphorous;
    @JsonProperty("soil_type")
    private String soilType;
    @JsonProperty("crop_type")
    private String cropType;
}
