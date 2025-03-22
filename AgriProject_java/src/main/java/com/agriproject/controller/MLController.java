package com.agriproject.controller;


import com.agriproject.dto.ML5Dto;
import com.agriproject.service.MLService;
import com.agriproject.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ml")
public class MLController {

    @Autowired
    private MLService mlService;



    @PostMapping("/5")
    public ResponseEntity<?> getPrediction(@RequestBody ML5Dto ml5Dto) {


        String response  =   mlService.predictM5(ml5Dto);

        Map<String, Object> map = new HashMap<>();
        map.put("response", response);
        return CommonUtil.createBuildResponse(map, HttpStatus.OK);

    }

}
