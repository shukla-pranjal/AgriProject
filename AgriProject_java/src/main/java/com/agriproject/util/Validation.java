package com.agriproject.util;


import com.agriproject.dto.ML5Dto;
import com.agriproject.dto.ML6Dto;
import com.agriproject.dto.ML7Dto;
import com.agriproject.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.agriproject.util.Constants.ALLOWED_CROP_TYPES;
import static com.agriproject.util.Constants.ALLOWED_SOIL_TYPES;

@Component
public class Validation {

    public void model5Validation(ML5Dto ml5Dto) {
        Map<String, Object> error = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(ml5Dto)) {
            throw new IllegalArgumentException("Category Object/JSON should not be empty");
        }
        if (ml5Dto.getN() == null || ml5Dto.getN() < 0 || ml5Dto.getN() > 145) {
            error.put("n", "Nitrogen (N) must be between 0 and 145");
        }

        // Phosphorus (P) validation
        if (ml5Dto.getP() == null || ml5Dto.getP() < 0 || ml5Dto.getP() > 150) {
            error.put("p", "Phosphorus (P) must be between 0 and 150");
        }

        // Potassium (K) validation
        if (ml5Dto.getK() == null || ml5Dto.getK() < 0 || ml5Dto.getK() > 210) {
            error.put("k", "Potassium (K) must be between 0 and 210");
        }

        // Temperature validation
        if (ml5Dto.getTemperature() == null || ml5Dto.getTemperature() < 8 || ml5Dto.getTemperature() > 44) {
            error.put("temperature", "Temperature must be between 8°C and 44°C");
        }

        // Humidity validation
        if (ml5Dto.getHumidity() == null || ml5Dto.getHumidity() < 14 || ml5Dto.getHumidity() > 99.99) {
            error.put("humidity", "Humidity must be between 14% and 99.99%");
        }

        // pH validation
        if (ml5Dto.getPh() == null || ml5Dto.getPh() < 3 || ml5Dto.getPh() > 10) {
            error.put("ph", "pH must be between 3 and 10");
        }

        // Rainfall validation
        if (ml5Dto.getRainfall() == null || ml5Dto.getRainfall() < 10 || ml5Dto.getRainfall() > 300) {
            error.put("rainfall", "Rainfall must be between 10mm and 300mm");
        }

        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }

    }


    public void model6Validation(ML6Dto ml6Dto) {
        Map<String, Object> error = new LinkedHashMap<>();

        // Check if the object is null
        if (ObjectUtils.isEmpty(ml6Dto)) {
            throw new IllegalArgumentException("ML6Dto JSON object should not be empty");
        }

        // Moisture validation
        if (ml6Dto.getMoisture() == null || ml6Dto.getMoisture() < 4 || ml6Dto.getMoisture() > 1023) {
            error.put("moisture", "Moisture must be between 4 and 1023");
        }

        // Temperature validation
        if (ml6Dto.getTemp() == null || ml6Dto.getTemp() < 9 || ml6Dto.getTemp() > 46) {
            error.put("temp", "Temperature must be between 9°C and 46°C");
        }

        // If any validation errors exist, throw a ValidationException
        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
    }


    public void model7Validation(ML7Dto ml7Dto) {
        Map<String, Object> errors = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(ml7Dto)) {
            throw new IllegalArgumentException("ML7Dto JSON object should not be empty");
        }

        validateRange(ml7Dto.getTemparature(), 24, 39, "temparature", errors);
        validateRange(ml7Dto.getHumidity(), 49, 73, "humidity", errors);
        validateRange(ml7Dto.getMoisture(), 24, 66, "moisture", errors);
        validateRange(ml7Dto.getNitrogen(), 3, 43, "nitrogen", errors);
        validateRange(ml7Dto.getPotassium(), 0, 20, "potassium", errors);
        validateRange(ml7Dto.getPhosphorous(), 0, 43, "phosphorous", errors);

        validateCategory(ml7Dto.getSoilType(), ALLOWED_SOIL_TYPES, "soilType", errors);
        validateCategory(ml7Dto.getCropType(), ALLOWED_CROP_TYPES, "cropType", errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateRange(Integer value, int min, int max, String fieldName, Map<String, Object> errors) {
        if (value == null) {
            errors.put(fieldName, fieldName + " should not be null");
        } else if (value < min || value > max) {
            errors.put(fieldName, fieldName + " must be between " + min + " and " + max);
        }
    }

    private void validateCategory(String value, List<String> validValues, String fieldName, Map<String, Object> errors) {
        if (ObjectUtils.isEmpty(value)) {
            errors.put(fieldName, fieldName + " should not be empty");
        } else if (!validValues.contains(value)) {
            errors.put(fieldName, fieldName + " must be one of " + validValues);
        }
    }



}
