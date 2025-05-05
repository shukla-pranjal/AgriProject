package com.tempagriproject2.dto;

import com.tempagriproject2.entity.Product;
import com.tempagriproject2.entity.User;
import jakarta.persistence.ManyToOne;
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
