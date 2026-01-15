package com.farmflow.controller;

import com.farmflow.endpoint.AdminEndpoint;
import com.farmflow.service.UserService;
import com.farmflow.service.email.EmailService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController implements AdminEndpoint {
    private final UserService userService;
    private final EmailService emailService;

    @Override
    public ResponseEntity<?> promote(Integer userId) throws Exception {
        userService.promote(userId);
        return CommonUtil.createBuildResponse("User promoted successfully", HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> demote(Integer userId) throws Exception {
        userService.demote(userId);
        return CommonUtil.createBuildResponse("User demoted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> toggleEmailService() {
        emailService.toggleEmailService();
        return CommonUtil.createBuildResponse("Email service toggle done", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getEmailServiceStatus() {
        Boolean bool = emailService.isEmailServiceEnabled();
        return CommonUtil.createBuildResponse(bool, HttpStatus.OK);
    }
}