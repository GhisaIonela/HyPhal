package com.company.validators;

import com.company.domain.User;
import com.company.exceptions.ValidationException;

/**
 * UserValidator validates an object of type User
 */
public class UserValidator implements Validator<User>{
    /**
     * Validates a user
     * To be valid, user's id cannot be null or negative.
     * First name and last name also cannot be null or longer than 100 characters
     * @param entity - the user to be validated
     * @throws ValidationException if the user is not valid
     */
    @Override
    public void validate(User entity) throws ValidationException {
        if(entity.getFirstName()==null){
            throw new ValidationException("First name cannot be null");
        }
        if(entity.getLastName()==null){
            throw new ValidationException("Last name cannot be null");
        }
        if(entity.getFirstName().length()>100){
            throw new ValidationException("First name cannot be longer than 100 characters");
        }
        if(entity.getLastName().length()>100){
            throw new ValidationException("Last name cannot be longer than 100 characters");
        }
        if(entity.getCity()==null){
            throw new ValidationException("City must not be null");
        }
        if(entity.getDateOfBirth()==null){
            throw new ValidationException("Date of birth must not be null");
        }
        if(entity.getUserCredentials()==null) {
            throw new ValidationException("Credentials must not be null");
        }
    }
}
