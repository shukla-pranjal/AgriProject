package com.farmflow.util;

import com.farmflow.config.security.CustomUserDetails;
import com.farmflow.entity.User;
import com.farmflow.handler.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

    public static User getLoggedInUser() {
        try{

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null || authentication.getPrincipal().equals("anonymousUser")) {
                log.warn("No authenticated user found, returning null.");
                return null;  // Or return a default User object if needed
            }

            CustomUserDetails logUser = (CustomUserDetails) authentication.getPrincipal();
            return logUser.getUser();


        }catch (Exception e){
            log.error("Error while generating secret key: {}", e.getMessage(), e);            throw e;
        }
    }
}
