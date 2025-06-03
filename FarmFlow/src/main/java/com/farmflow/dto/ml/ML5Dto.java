package com.farmflow.dto.ml;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ML5Dto {
    private Integer id;
    private Integer n;
    private Integer p;
    private Integer k;
    private Double temperature;
    private Double humidity;
    private Double ph;
    private Double rainfall ;
}
