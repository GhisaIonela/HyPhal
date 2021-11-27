package com.company.exceptions;

public class RepositoryDbException extends RuntimeException{
    /**
     * Creates a RepositoryDbException exception with specific message
     * @param message - message string
     */

    public RepositoryDbException(String message) {
        super(message);
    }
}
