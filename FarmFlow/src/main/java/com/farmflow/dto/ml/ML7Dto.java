package com.farmflow.dto.ml;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "Data Transfer Object for ML7 model input (crop health prediction)")
public class ML7Dto {

    @Schema(description = "Unique identifier for the ML7 data entry", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer id;

    @Schema(description = "Temperature in degrees Celsius", example = "30", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "24", maximum = "39")
    private int temparature;

    @Schema(description = "Humidity percentage", example = "60", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "49", maximum = "73")
    private int humidity;

    @Schema(description = "Moisture level (sensor reading)", example = "50", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "24", maximum = "66")
    private int moisture;

    @Schema(description = "Nitrogen level in the soil", example = "30", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "3", maximum = "43")
    private int nitrogen;

    @Schema(description = "Potassium level in the soil", example = "15", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0", maximum = "20")
    private int potassium;

    @Schema(description = "Phosphorus level in the soil", example = "20", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0", maximum = "43")
    private int phosphorous;

    @JsonProperty("soil_type")
    @Schema(description = "Type of soil", example = "Loamy", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"Sandy", "Loamy", "Clay", "Silty"}) // Adjust based on Constants.ALLOWED_SOIL_TYPES
    private String soilType;

    @JsonProperty("crop_type")
    @Schema(description = "Type of crop", example = "Wheat", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"Wheat", "Rice", "Maize", "Barley"}) // Adjust based on Constants.ALLOWED_CROP_TYPES
    private String cropType;
}