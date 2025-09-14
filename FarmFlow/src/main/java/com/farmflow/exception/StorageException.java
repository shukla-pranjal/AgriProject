package com.farmflow.exception;

public class StorageException extends RuntimeException {
    /**
     * Thrown when a storage operation (upload/download/delete) fails.
     */
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}