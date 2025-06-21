package com.farmflow.exception;


import com.farmflow.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sqm.produce.function.FunctionArgumentException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingRequestParam(MissingServletRequestParameterException ex) {
        log.error("MissingServletRequestParameterException: {}", ex.getMessage());
        String message = "Missing required query parameter: '" + ex.getParameterName() + "'";
        return CommonUtil.createErrorResponseMessage(message, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageException(StorageException ex) {
        log.error("StorageException: {}", ex.getMessage());
        return CommonUtil.createErrorResponseMessage(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<?> handleJwtTokenExpiredException(JwtTokenExpiredException ex) {
        log.error("JwtTokenExpiredException: {}", ex.getMessage());
        return CommonUtil.createErrorResponseMessage(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatchException: {}", ex.getMessage());
        String param = ex.getName();
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        String message = String.format("Invalid value for parameter '%s'. Expected type: %s", param, requiredType);
        return CommonUtil.createErrorResponseMessage(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<?> handleMissingPathVariable(MissingPathVariableException ex) {
        log.error("MissingPathVariableException: {}", ex.getMessage());
        String message = "Missing path variable: '" + ex.getVariableName() + "'";
        return CommonUtil.createErrorResponseMessage(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("AccessDeniedException: {}", ex.getMessage());
        return CommonUtil.createErrorResponseMessage("Access Denied: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("GlobalExceptionHandler : handleMethodNotSupportedException() : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(
                "Invalid HTTP method! Expected: " + e.getSupportedHttpMethods(),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }


    @ExceptionHandler(FlaskServiceException.class)
    public ResponseEntity<?> handleFlaskServiceException(FlaskServiceException e) {
        log.error("GlobalExceptionHandler : handleFlaskServiceException() : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage("Error while handling Flask Service Exception " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
        log.error("NoHandlerFoundException: {}", ex.getMessage());
        return CommonUtil.createErrorResponseMessage(
                "No endpoint found for path: " + request.getRequestURI(),
                HttpStatus.NOT_FOUND
        );
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

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<?> handleInsufficientStockException(InsufficientStockException e){
        log.error("GlobalExceptionHandler : InsufficientStockException() : {}", e.getMessage());
        return CommonUtil.createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }
@ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResourceException(DuplicateResourceException e){
        log.error("GlobalExceptionHandler : DuplicateResourceException() : {}", e.getMessage());
        return CommonUtil.createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException
                                                                     e){
        log.error("ResourceNotFoundException : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e){
        log.error("Exception : {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.FORBIDDEN);
    }

}