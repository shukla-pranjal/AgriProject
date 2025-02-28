package com.agriproject.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ML5Dto {
    private Integer id;
    private Integer N;
    private Integer P;
    private Integer K;
    private Double temperature;
    private Double humidity;
    private Double ph;
    private Double rainfall ;


}
