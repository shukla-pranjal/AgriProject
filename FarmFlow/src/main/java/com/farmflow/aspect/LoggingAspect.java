package com.farmflow.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // Common method to log execution details
    private Object logExecution(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        // Extract method and class details
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String executionContext = String.format("%s.%s()", className, methodName);

        // Get contextual information
        String requestId = getRequestId();
        String userId = getUserId();
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);

        // Log method entry with arguments
        Object[] args = joinPoint.getArgs();
        log.info("[{}] [{}] [{}] [{}] Entering method - Args: {}",
                timestamp, requestId, userId, executionContext, Arrays.toString(args));

        // Measure execution time
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            // Log method exit with result and duration
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

    // Helper method to get a unique request ID without using UUID
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

    // Helper method to get the authenticated user ID
    private String getUserId() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof String) {
                return (String) principal; // e.g., user ID or username
            } else if (principal != null) {
                // Assuming principal is a custom UserDetails object with a getId() method
                return principal.toString();
            }
        } catch (Exception e) {
            log.warn("Could not retrieve user ID: {}", e.getMessage());
        }
        return "anonymous";
    }
}