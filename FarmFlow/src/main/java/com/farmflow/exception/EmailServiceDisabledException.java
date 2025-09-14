package com.farmflow.exception;


public class EmailServiceDisabledException extends RuntimeException {
    public EmailServiceDisabledException() {
        this("Email service is currently disabled by admin.");
    }
    public EmailServiceDisabledException(String message){
        super(message);
    }

}
