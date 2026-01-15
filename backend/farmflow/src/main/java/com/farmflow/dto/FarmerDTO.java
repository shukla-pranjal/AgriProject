package com.farmflow.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Data Transfer Object for a farmer")
public class FarmerDTO {

    @Schema(description = "Unique identifier for the farmer", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private int id;

    @Schema(description = "ID of the user associated with the farmer", example = "1", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1")
    private Integer userId;

    @Schema(description = "Name of the farm", example = "Green Acres", requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 2, maxLength = 50)
    private String farmName;

    @Schema(description = "Type of the farm", example = "Organic", requiredMode = Schema.RequiredMode.REQUIRED)
    private String farmType;

    @Schema(description = "Description of the farm's location", example = "Near River Road, Village X",
            requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 200)
    private String locationDiscription;

    @Schema(description = "Government-issued ID of the farmer", example = "ABC123456789",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String governmentId;

    @ArraySchema(
            schema = @Schema(implementation = ImageDTO.class, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    )
    @Schema(description = "List of images associated with the farmer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<ImageDTO> images = new ArrayList<>();
}