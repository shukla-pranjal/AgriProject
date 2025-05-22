package com.farmflow.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AddressDTO {
    private Integer id;
    private Integer pinCode;
    private String street;
    private String district;
    private Integer addressType;
    private Integer state;
    private Integer userId;
}
