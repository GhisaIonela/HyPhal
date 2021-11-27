package com.company.exceptions;

public class UserNotFoundException extends RuntimeException{

    /**
     * Creates a UserNotFoundException exception with specific message
     * @param message - message string
     */

    public UserNotFoundException(String message) {
        super(message);
    }
}
