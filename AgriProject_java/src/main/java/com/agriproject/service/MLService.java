package com.agriproject.service;

import com.agriproject.dto.ML5Dto;
import org.springframework.stereotype.Service;

//@Service
public interface MLService {


    String predictM5(ML5Dto ml5Dto);
}
