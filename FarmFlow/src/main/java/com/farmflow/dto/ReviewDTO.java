package com.farmflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "Data Transfer Object for a product review")
public class ReviewDTO {

    @Schema(description = "Unique identifier for the review", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private int id;

    @Schema(description = "Rating given in the review (1 to 5)", example = "4", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1", maximum = "5")
    private Integer rating;

    @Schema(description = "Comment provided in the review", example = "Great product!", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 300)
    private String comment;

    @Schema(description = "ID of the product being reviewed", example = "1", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1")
    private Integer productId;

    @Schema(description = "ID of the user who submitted the review", example = "1", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1")
    private Integer userId;
}