package com.farmflow.controller;

import com.farmflow.dto.LoginRequest;
import com.farmflow.dto.LoginResponse;
import com.farmflow.dto.UserDTO;
import com.farmflow.endpoint.AuthEndpoint;
import com.farmflow.service.AuthService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthEndpoint {
    private final AuthService authService;
    private static final String CLASS_NAME = AuthController.class.getName();

    @Override
    public ResponseEntity<?> create(UserDTO userDTO) {
        String methodName = "create";
        log.debug("{} : {} :: Started with userDTO: {}", CLASS_NAME, methodName, userDTO);
        UserDTO created = authService.createUser(userDTO);
        log.info("{} : {} :: Successfully created user", CLASS_NAME, methodName);
        return CommonUtil.createBuildResponse(created, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        LoginResponse loginResponse =  authService.login(loginRequest);
        String methodName = "login";
        log.debug("{} : {} :: Started with loginRequest: {}", CLASS_NAME, methodName, loginRequest);
        if (ObjectUtils.isEmpty(loginResponse)) {
            return CommonUtil.createErrorResponseMessage("Invalid Credential", HttpStatus.BAD_REQUEST);
        }
        return CommonUtil.createBuildResponse(loginResponse, HttpStatus.CREATED);
    }
}
