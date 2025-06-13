package com.farmflow.controller;

import com.farmflow.dto.LoginRequest;
import com.farmflow.dto.LoginResponse;
import com.farmflow.dto.UserDTO;
import com.farmflow.endpoint.AuthEndpoint;
import com.farmflow.service.AuthService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController implements AuthEndpoint {
    private final AuthService authService;

    @Override
    public ResponseEntity<?> create(UserDTO userDTO) {
        UserDTO created = authService.createUser(userDTO);
        return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        if (ObjectUtils.isEmpty(loginResponse)) {
            return CommonUtil.createErrorResponseMessage("Invalid Credential", HttpStatus.BAD_REQUEST);
        }
        return CommonUtil.createBuildResponse(loginResponse, HttpStatus.CREATED);
    }
}