package com.farmflow.exception;


import lombok.Getter;

@Getter
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message){
        super(message);
    }

}
