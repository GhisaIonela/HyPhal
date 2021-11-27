package com.company.validators;

import com.company.exceptions.ValidationException;

/**
 * Validation interface
 * @param <T> - the type of object that needs to be validated
 */
public interface Validator<T> {
    /**
     * Validates a T type object
     * @param entity - the object to be validated
     * @throws ValidationException if the entity is not valid
     */
    void validate(T entity) throws ValidationException;
}