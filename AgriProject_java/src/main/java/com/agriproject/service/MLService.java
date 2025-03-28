package com.agriproject.service;

import com.agriproject.dto.ML5Dto;
import com.agriproject.dto.ML6Dto;
import com.agriproject.dto.ML7Dto;


public interface MLService {

    String predictM5(ML5Dto ml5Dto);

    String predictM6(ML6Dto ml6Dto);

    String predictM7(ML7Dto ml7Dto);
}
