package com.farmflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewDTO {
    private int id;
    private Integer rating;
    private String comment;
    private Integer productId;
    private Integer userId;
}
