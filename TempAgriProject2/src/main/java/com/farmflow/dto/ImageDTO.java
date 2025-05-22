package com.farmflow.dto;

import com.farmflow.enums.EntityType;
import com.farmflow.enums.FileType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {
    private int id;
    private String fileName;
    private String savedName;
    private FileType fileType;
    private Long fileSize;
    private EntityType entityType;
    private Integer entityId;
    private Boolean isPrimary;
}
