package com.tempagriproject2.exception;


import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {
    private Map<String, Object> error;

    public ValidationException(Map<String, Object> error){
        super("Validation Failed");
        this.error = error;
    }

}
