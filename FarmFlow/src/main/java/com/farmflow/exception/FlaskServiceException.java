package com.farmflow.exception;

public class FlaskServiceException extends RuntimeException {
    public FlaskServiceException(String message) {
        super(message);
    }
}
