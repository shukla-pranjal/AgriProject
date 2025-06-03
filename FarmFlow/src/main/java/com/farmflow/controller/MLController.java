package com.farmflow.controller;

import com.farmflow.dto.ml.ML5Dto;
import com.farmflow.dto.ml.ML6Dto;
import com.farmflow.dto.ml.ML7Dto;
import com.farmflow.endpoint.MLEndpoint;
import com.farmflow.service.MLService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MLController implements MLEndpoint {

    private final MLService mlService;
    private static final String CLASS_NAME = MLController.class.getSimpleName();

    @Override
    public ResponseEntity<?> getPredictionModel5(ML5Dto ml5Dto) {
        String methodName = "getPredictionModel5";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);

        String response = mlService.predictM5(ml5Dto);
        Map<String, Object> map = new HashMap<>();
        map.put("crop", response);

        log.info("{} : {} :: Prediction completed", CLASS_NAME, methodName);
        return CommonUtil.createBuildResponse(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getPredictionModel6(ML6Dto ml6Dto) {
        String methodName = "getPredictionModel6";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);

        Boolean response = Boolean.valueOf(mlService.predictM6(ml6Dto));
        Map<String, Object> map = new HashMap<>();
        map.put("pump", response);

        log.info("{} : {} :: Prediction completed", CLASS_NAME, methodName);
        return CommonUtil.createBuildResponse(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getPredictionModel7(ML7Dto ml7Dto) {
        String methodName = "getPredictionModel7";
        log.debug("{} : {} :: Started", CLASS_NAME, methodName);

        String response = mlService.predictM7(ml7Dto);
        Map<String, Object> map = new HashMap<>();
        map.put("fertilizer", response);

        log.info("{} : {} :: Prediction completed", CLASS_NAME, methodName);
        return CommonUtil.createBuildResponse(map, HttpStatus.OK);
    }
}

