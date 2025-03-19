package com.agriproject.controller;


import com.agriproject.dto.ML5Dto;
import com.agriproject.dto.ML6Dto;
import com.agriproject.dto.ML7Dto;
import com.agriproject.service.MLService;
import com.agriproject.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ml")
public class MLController {

    @Autowired
    private MLService mlService;



    @PostMapping("/5")
    public ResponseEntity<?> getPredictionModel5(@RequestBody ML5Dto ml5Dto) {
        String response  =   mlService.predictM5(ml5Dto);

        Map<String, Object> map = new HashMap<>();

        map.put("crop", response);
        return CommonUtil.createBuildResponse(map, HttpStatus.OK);
    }


    @PostMapping("/6")
    public ResponseEntity<?> getPredictionModel6(@RequestBody ML6Dto ml6Dto) {

        Boolean response  = Boolean.valueOf(mlService.predictM6(ml6Dto));

        Map<String, Object> map = new HashMap<>();

        map.put("pump", response);
        return CommonUtil.createBuildResponse(map, HttpStatus.OK);

    }

    @PostMapping("/7")
    public ResponseEntity<?> getPredictionModel7(@RequestBody ML7Dto ml7Dto) {
        String response = mlService.predictM7(ml7Dto);

        Map<String, Object> map = new HashMap<>();
        map.put("fertilizer", response);
        return CommonUtil.createBuildResponse(map, HttpStatus.OK);
    }





}
