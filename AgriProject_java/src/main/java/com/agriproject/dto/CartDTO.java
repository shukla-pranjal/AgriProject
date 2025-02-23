package com.agriproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private ProductDTO product;
    private UserDTO user;
    private int rating;
    private String comment;
    private Date reviewDate;
}
