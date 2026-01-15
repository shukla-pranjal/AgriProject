package com.farmflow.aspect;

import com.farmflow.entity.User;
import com.farmflow.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Random RANDOM = new Random();

    private Object logExecution(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String executionContext = String.format("%s.%s()", className, methodName);

        String requestId = getRequestId();
        String userId = getUserIdentifier(); // Use enhanced user ID retrieval
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);

        Object[] args = joinPoint.getArgs();
        log.info("[{}] [{}] [{}] [{}] Entering method - Args: {}",
                timestamp, requestId, userId, executionContext, Arrays.toString(args));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            log.info("[{}] [{}] [{}] [{}] Exiting method - Duration: {} ms, Result: {}",
                    timestamp, requestId, userId, executionContext, duration, result != null ? result : "void");

            return result;
        } catch (Throwable throwable) {
            // Log exceptions with stack trace
            long duration = System.currentTimeMillis() - startTime;
            log.error("[{}] [{}] [{}] [{}] Exception in method - Duration: {} ms, Error: {}",
                    timestamp, requestId, userId, executionContext, duration, throwable.getMessage(), throwable);
            throw throwable;
        }
    }

    @Around("execution(* com.farmflow.controller..*(..))")
    public Object aspectController(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(joinPoint, "Controller");
    }

    @Around("execution(* com.farmflow.service..*(..))")
    public Object aspectService(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(joinPoint, "Service");
    }

    // Helper method to get a unique request ID
    private String getRequestId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String requestId = (String) request.getAttribute("requestId");
                if (requestId == null) {
                    // Generate a unique request ID using System.nanoTime() and a random number
                    long nanoTime = System.nanoTime();
                    int randomPart = RANDOM.nextInt(10000); // Random number between 0 and 9999
                    requestId = String.format("req-%d-%d", nanoTime, randomPart);
                    request.setAttribute("requestId", requestId);
                }
                return requestId;
            }
        } catch (Exception e) {
            log.warn("Could not retrieve request ID: {}", e.getMessage());
        }
        return "unknown-request";
    }

    // Helper method to get user identifier using CommonUtil.getLoggedInUser()
    private String getUserIdentifier() {
        try {
            User loggedInUser = CommonUtil.getLoggedInUser();
            if (loggedInUser != null) {
                return String.valueOf(loggedInUser.getId()); // Assuming User has a getId() method
            }
            log.warn("No authenticated user found, falling back to anonymous.");
            return "anonymous";
        } catch (Exception e) {
            log.warn("Could not retrieve user identifier: {}", e.getMessage());
            return "anonymous";
        }
    }
}