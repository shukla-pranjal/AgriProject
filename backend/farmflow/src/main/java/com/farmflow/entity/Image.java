package com.farmflow.entity;

import com.farmflow.enums.EntityType;
import com.farmflow.enums.FileType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Image extends BaseModel {
    private String fileName;
    private String savedName;
    @Enumerated(EnumType.STRING)
    private FileType fileType;
    private Long fileSize;
    @Enumerated(EnumType.STRING)
    private EntityType entityType;
    private Integer entityId;
    private Boolean isPrimary;
}
