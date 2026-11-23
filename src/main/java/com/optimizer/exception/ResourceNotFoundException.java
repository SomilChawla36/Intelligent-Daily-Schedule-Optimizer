package com.optimizer.exception;

/**
 * Thrown when a requested resource (Student, Subject, Mark) is not found in the database.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
