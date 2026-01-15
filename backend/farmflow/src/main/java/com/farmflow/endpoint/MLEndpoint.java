package com.farmflow.endpoint;

import com.farmflow.dto.ml.ML5Dto;
import com.farmflow.dto.ml.ML6Dto;
import com.farmflow.dto.ml.ML7Dto;
import com.farmflow.handler.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Machine Learning Predictions", description = "Endpoints for machine learning-based predictions for crop recommendation, pump operation, and fertilizer selection")
@RequestMapping("/api/v1/ml")
public interface MLEndpoint {

    @Operation(summary = "Predict Crop Recommendation (Model 5)", description = "Uses ML Model 5 to predict the best crop based on input parameters like soil and weather conditions. Accessible by admins and farmers.")
    @ApiResponse(responseCode = "200", description = "Successfully predicted the crop",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = GenericResponse.class)))
    @PostMapping("/5")
    ResponseEntity<?> getPredictionModel5(
            @Parameter(description = "Input data for Model 5 prediction (e.g., soil pH, temperature)", required = true) @RequestBody ML5Dto ml5Dto
    );

    @Operation(summary = "Predict Pump Operation (Model 6)", description = "Uses ML Model 6 to predict whether a pump should be turned on based on input parameters like soil moisture and weather. Accessible by admins and farmers.")
    @ApiResponse(responseCode = "200", description = "Successfully predicted pump operation",
            content = @Content(mediaType = "application/json"))
    @PostMapping("/6")
    ResponseEntity<?> getPredictionModel6(
            @Parameter(description = "Input data for Model 6 prediction (e.g., soil moisture, rainfall)", required = true) @RequestBody ML6Dto ml6Dto
    );

    @Operation(summary = "Predict Fertilizer Recommendation (Model 7)", description = "Uses ML Model 7 to predict the best fertilizer based on input parameters like soil nutrients and crop type. Accessible by admins and farmers.")
    @ApiResponse(responseCode = "200", description = "Successfully predicted fertilizer",
            content = @Content(mediaType = "application/json"))
    @PostMapping("/7")
    ResponseEntity<?> getPredictionModel7(
            @Parameter(description = "Input data for Model 7 prediction (e.g., soil NPK levels, crop type)", required = true) @RequestBody ML7Dto ml7Dto
    );
}