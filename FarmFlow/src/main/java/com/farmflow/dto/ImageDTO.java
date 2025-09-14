package com.farmflow.dto;

import com.farmflow.enums.EntityType;
import com.farmflow.enums.FileType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object for an image associated with an entity")
public class ImageDTO {

    @Schema(description = "Unique identifier for the image", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private int id;

    @Schema(description = "Original name of the uploaded file", example = "image.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileName;

    @Schema(description = "Name under which the file is stored", example = "image_20250603_1511.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String savedName;

    @Schema(description = "Type of the file", example = "IMAGE_PNG", requiredMode = Schema.RequiredMode.REQUIRED)
    private FileType fileType;

    @Schema(description = "Size of the file in bytes", example = "102400", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long fileSize;

    @Schema(description = "Type of entity associated with the image", example = "FARMER", requiredMode = Schema.RequiredMode.REQUIRED)
    private EntityType entityType;

    @Schema(description = "ID of the entity associated with the image", example = "1", requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1")
    private Integer entityId;

    @Schema(description = "Indicates if this is the primary image for the entity", example = "true", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean isPrimary;
}