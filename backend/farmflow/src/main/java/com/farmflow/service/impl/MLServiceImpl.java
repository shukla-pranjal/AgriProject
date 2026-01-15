package com.farmflow.service.impl;


import com.farmflow.dto.ml.ML5Dto;
import com.farmflow.dto.ml.ML6Dto;
import com.farmflow.dto.ml.ML7Dto;
import com.farmflow.exception.FlaskServiceException;
import com.farmflow.service.MLService;
import com.farmflow.service.external.FlaskService;
import com.farmflow.util.Validation;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

@Service
public class MLServiceImpl  implements MLService {


    @Autowired
    private FlaskService flaskService;

    @Autowired
    private Validation validation;


    private <T> String getPrediction(Function<T, Map<String, Object>> flaskMethod, T requestDto) {
        try {
            Map<String, Object> response = flaskMethod.apply(requestDto);
            return (String) response.get("response"); // Extract "response"
        } catch (FeignException e) {
            throw e;
        }
        catch (Exception e) {
            throw new FlaskServiceException(e.getMessage());
        }
    }

    @Override
    public String predictM5(ML5Dto ml5Dto) {
        validation.model5Validation(ml5Dto);
        return getPrediction(flaskService::model5, ml5Dto);
    }

    @Override
    public String predictM6(ML6Dto ml6Dto) {
        validation.model6Validation(ml6Dto);
        return getPrediction(flaskService::model6, ml6Dto);
    }

    @Override
    public String predictM7(ML7Dto ml7Dto) {
        validation.model7Validation(ml7Dto);
        return getPrediction(flaskService::model7, ml7Dto);
    }
}