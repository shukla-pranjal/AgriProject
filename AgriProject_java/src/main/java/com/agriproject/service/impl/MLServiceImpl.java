package com.agriproject.service.impl;

import com.agriproject.dto.ML5Dto;
import com.agriproject.service.MLService;
import com.agriproject.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import static com.agriproject.util.Constants.FLASK_API_URL;


@Service
public class MLServiceImpl implements MLService {


    @Override
    public String predictM5(ML5Dto ml5Dto) {
        RestTemplate restTemplate = new RestTemplate();

        String url = FLASK_API_URL + "5";

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, ml5Dto, String.class);

            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to get response from Flask!";
        }
    }
}
