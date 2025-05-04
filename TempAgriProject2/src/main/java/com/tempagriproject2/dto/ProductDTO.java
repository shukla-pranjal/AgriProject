package com.tempagriproject2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}


