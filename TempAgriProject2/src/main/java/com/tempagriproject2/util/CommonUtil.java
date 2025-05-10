package com.tempagriproject2.util;

import com.tempagriproject2.handler.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class CommonUtil {

    public static <T> ResponseEntity<?> createBuildResponse(T data, HttpStatus status) {
        GenericResponse<T> response = GenericResponse.<T>builder()
                .responseStatus(status)
                .status(Constants.STATUS_SUCCESS)
                .message(Constants.MESSAGE_SUCCESS)
                .data(data)
                .build();
        return response.create();
    }

    public static ResponseEntity<?> createBuildResponseMessage(String message, HttpStatus status) {
        GenericResponse<Void> response = GenericResponse.<Void>builder()
                .responseStatus(status)
                .status(Constants.STATUS_SUCCESS)
                .message(message)
                .build();
        return response.create();
    }

    public static <T> ResponseEntity<?> createErrorResponse(T data, HttpStatus status) {
        GenericResponse<T> response = GenericResponse.<T>builder()
                .responseStatus(status)
                .status(Constants.STATUS_FAILED)
                .message(Constants.MESSAGE_FAILED)
                .data(data)
                .build();
        return response.create();
    }

    public static ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus status) {
        GenericResponse<Void> response = GenericResponse.<Void>builder()
                .responseStatus(status)
                .status(Constants.STATUS_FAILED)
                .message(message)
                .build();
        return response.create();
    }
}
