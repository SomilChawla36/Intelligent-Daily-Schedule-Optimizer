package com.optimizer.exception;

/**
 * Custom Exception thrown when an invalid mark value is provided
 * (e.g., negative mark or marks exceeding maximum possible marks).
 */
public class InvalidMarksException extends RuntimeException {
    public InvalidMarksException(String message) {
        super(message);
    }
}
