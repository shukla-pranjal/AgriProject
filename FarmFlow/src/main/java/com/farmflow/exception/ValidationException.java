package com.farmflow.exception;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {
    private Map<String, Object> error;

    public ValidationException(Map<String, Object> error){
        super("Validation Failed");
        this.error = error;
    }
    public ValidationException(String message){
        super("Validation Failed");
        this.error = new HashMap<>();
        this.error.put("message", message);
    }

}
