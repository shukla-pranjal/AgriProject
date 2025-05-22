package com.farmflow.controller;

import com.farmflow.endpoint.AdminEndpoint;
import com.farmflow.service.UserService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController implements AdminEndpoint{
    private final UserService userService;
    private static final String CLASS_NAME = UserController.class.getSimpleName();

    @Override
    public ResponseEntity<?> promote(Integer userId) throws Exception {
        String methodName = "promote";
        log.debug("{} : {} :: Started with userId: {}", CLASS_NAME, methodName, userId);
        try {
            userService.promote(userId);
            log.info("{} : {} :: Successfully promoted user with id: {}", CLASS_NAME, methodName, userId);
            return CommonUtil.createBuildResponse("User promoted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to promote user with id: {}, error: {}", CLASS_NAME, methodName, userId, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> demote(Integer userId) throws Exception {
        String methodName = "demote";
        log.debug("{} : {} :: Started with userId: {}", CLASS_NAME, methodName, userId);
        try {
            userService.demote(userId);
            log.info("{} : {} :: Successfully demoted user with id: {}", CLASS_NAME, methodName, userId);
            return CommonUtil.createBuildResponse("User demoted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("{} : {} :: Failed to demote user with id: {}, error: {}", CLASS_NAME, methodName, userId, e.getMessage());
            throw e;
        }
    }
}