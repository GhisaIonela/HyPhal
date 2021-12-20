package com.company.exceptions;

public class InvalidEmailExceptions extends RuntimeException{
    public InvalidEmailExceptions(String message) {
        super(message);
    }
}
