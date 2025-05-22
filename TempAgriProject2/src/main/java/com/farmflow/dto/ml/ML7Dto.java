package com.farmflow.dto.ml;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ML7Dto {
    private Integer id;
    private Integer moisture;
    private Integer temp;
}
