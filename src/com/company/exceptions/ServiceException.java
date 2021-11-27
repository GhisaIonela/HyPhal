package com.company.exceptions;

/**
 * ServiceExceptions is the class of those exceptions that can be thrown from services classes
 */
public class ServiceException extends RuntimeException {
    /**
     * Constructs a new exception
     */
    public ServiceException(){}

    /**
     * Constructs a new exception with the specified detail message
     * @param message the detail message to be thrown
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * @param message the detail message to be thrown
     * @param cause the cause
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause
     * @param cause the cause
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message, cause, suppression and stackTrace
     * @param message the detail message to be thrown
     * @param cause the cause
     * @param enableSuppression - whether or not suppression is enabled or disabled
     * @param writableStackTrace - whether or not the stack trace should be writable
     */
    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
