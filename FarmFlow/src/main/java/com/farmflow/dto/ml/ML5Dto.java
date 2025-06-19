package com.farmflow.dto.ml;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "Data Transfer Object for ML5 model input (crop recommendation based on soil and environmental factors)")
public class ML5Dto {

    @Schema(description = "Unique identifier for the ML5 data entry", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer id;

    @Schema(description = "Nitrogen (N) level in the soil", example = "100", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0", maximum = "145")
    private Integer n;

    @Schema(description = "Phosphorus (P) level in the soil", example = "50", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0", maximum = "150")
    private Integer p;

    @Schema(description = "Potassium (K) level in the soil", example = "150", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0", maximum = "210")
    private Integer k;

    @Schema(description = "Temperature in degrees Celsius", example = "25.5", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "8", maximum = "44")
    private Double temperature;

    @Schema(description = "Humidity percentage", example = "65.5", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "14", maximum = "99.99")
    private Double humidity;

    @Schema(description = "pH level of the soil", example = "6.5", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "3", maximum = "10")
    private Double ph;

    @Schema(description = "Rainfall in millimeters", example = "150.0", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "10", maximum = "300")
    private Double rainfall;
}