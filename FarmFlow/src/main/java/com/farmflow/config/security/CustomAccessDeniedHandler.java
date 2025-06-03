package com.farmflow.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.farmflow.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        // Use CommonUtil to create a standardized error response
        ResponseEntity<?> errorResponse = CommonUtil.createErrorResponseMessage(
                "Access denied: " + request.getRequestURI() + " " + accessDeniedException.getMessage(),
                HttpStatus.FORBIDDEN
        );

        // Write the response to the output stream
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse.getBody());
        response.flushBuffer();
    }
}