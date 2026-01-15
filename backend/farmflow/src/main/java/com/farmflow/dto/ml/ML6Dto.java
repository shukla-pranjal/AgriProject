package com.farmflow.dto.ml;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "Data Transfer Object for ML6 model input (environmental monitoring)")
public class ML6Dto {

    @Schema(description = "Unique identifier for the ML6 data entry", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer id;

    @Schema(description = "Moisture level (sensor reading)", example = "500", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "4", maximum = "1023")
    private Integer moisture;

    @Schema(description = "Temperature in degrees Celsius", example = "25", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "9", maximum = "46")
    private Integer temp;
}