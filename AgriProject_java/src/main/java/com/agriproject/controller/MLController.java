package com.agriproject.controller;


import com.agriproject.dto.ML5Dto;
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
    private final RestTemplate restTemplate = new RestTemplate();
    private final String FLASK_API_URL = "http://localhost:5000/predict"; // U

    @PostMapping("/5")
    public ResponseEntity<?> getPrediction(@RequestBody ML5Dto ml5Dto) {

        Map<String, Object> requestData = new HashMap<>();
        System.out.println(ml5Dto);
        requestData.put("N", ml5Dto.getN());
        requestData.put("P", ml5Dto.getP());
        requestData.put("K", ml5Dto.getK());
        requestData.put("temperature", ml5Dto.getTemperature());
        requestData.put("humidity", ml5Dto.getHumidity());
        requestData.put("ph", ml5Dto.getPh());
        requestData.put("rainfall", ml5Dto.getRainfall());

        // Call the Flask API
        ResponseEntity<String> response = restTemplate.postForEntity(FLASK_API_URL, requestData, String.class);

        // Return the response from the ML model
        return ResponseEntity.ok(response.getBody());

    }

}
