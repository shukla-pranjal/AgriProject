package com.agriproject.exception;



import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;

import org.hibernate.query.sqm.produce.function.FunctionArgumentException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.agriproject.util.CommonUtil;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<?> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException e) {
        log.error("GlobalExceptionHandler : handleInvalidDataAccessApiUsageException() : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(
                "Invalid data access usage! " + e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    // Handle FunctionArgumentException (specific Hibernate exception)
    @ExceptionHandler(FunctionArgumentException.class)
    public ResponseEntity<?> handleFunctionArgumentException(FunctionArgumentException e) {
        log.error("GlobalExceptionHandler : handleFunctionArgumentException() : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(
                "Function argument mismatch: " + e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException e) {
        log.error("GlobalExceptionHandler : handleFeignException() : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FlaskServiceException.class)
    public ResponseEntity<?> handleFlaskServiceException(FlaskServiceException e) {
        log.error("GlobalExceptionHandler : handleFlaskServiceException() : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage("Error while handling Flask Service Exception " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("GlobalExceptionHandler : handleMethodNotSupportedException() : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(
                "Invalid HTTP method! Expected: " + e.getSupportedHttpMethods(),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(Exception e) {
        log.error("GlobalExceptionHandler : handleIllegalArgumentException() : {}", e.getMessage(), e);
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(Exception e) {
        log.error("GlobalExceptionHandler : handleNullPointerException() : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(Exception e){
        log.error("ResourceNotFoundException : HttpMessageNotReadableException() : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(Exception e){
        log.error("ResourceNotFoundException : FileNotFoundException() : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e){
        log.error("GlobalExceptionHandler : ValidationException() : {}", e.getMessage());
        return CommonUtil.createErrorResponse(e.getError(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e){
        log.error("ResourceNotFoundException : AccessDeniedException() : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(AccessDeniedException e){
        log.error("Exception : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException
    e){
        log.error("ResourceNotFoundException : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }


}
