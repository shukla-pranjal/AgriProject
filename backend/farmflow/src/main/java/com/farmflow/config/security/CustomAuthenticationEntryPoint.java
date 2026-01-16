package com.farmflow.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.farmflow.handler.GenericResponse;
import com.farmflow.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        GenericResponse<Void> responseObj = GenericResponse.<Void>builder()
                .responseStatus(HttpStatus.UNAUTHORIZED)
                .status(Constants.STATUS_FAILED)
                .message("Authentication failed: " + authException.getMessage())
                .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeVaCustomAuthenticationEntryPointlue(response.getOutputStream(), responseObj);
        response.flushBuffer();
    }
}

