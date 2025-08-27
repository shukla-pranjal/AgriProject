package com.farmflow.controller;

import com.farmflow.dto.*;
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

    @Override
    public ResponseEntity<?> verifyEmail(EmailVerificationRequest request) throws Exception {
        authService.verifyEmail(request);
        return CommonUtil.createBuildResponseMessage("Email Verified", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> resendVerification(
           ResendVerificationRequest request
    ) throws  Exception{

        authService.resendVerification(request);

        // Return success message upon successful resend
        return CommonUtil.createBuildResponseMessage("Verification email resent successfully", HttpStatus.OK);
        }




}