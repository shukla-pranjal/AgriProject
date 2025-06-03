package com.farmflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDTO {
    private int id;
    private String name;
    private String description;
    private Integer categoryId;
    private Double price;
    private Double quantity;
    private Integer unit;
    private LocalDateTime expiryDate;
    private Integer farmerId;
    private Boolean available;
    List<ImageDTO> images = new ArrayList<>();
}


