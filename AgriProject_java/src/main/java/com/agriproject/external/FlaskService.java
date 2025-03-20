package com.agriproject.external;


import com.agriproject.dto.ML5Dto;
import com.agriproject.dto.ML6Dto;
import com.agriproject.dto.ML7Dto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name= "ML-SERVICE")
public interface FlaskService {


    @PostMapping("/predict/5")
    Map<String, Object> model5(@RequestBody ML5Dto ml5Dto);

    @PostMapping("/predict/6")
    Map<String, Object> model6(@RequestBody ML6Dto ml6Dto);

    @PostMapping("/predict/7")
    Map<String, Object> model7(@RequestBody ML7Dto ml7Dto);



}
