package com.company.validators;

import com.company.domain.Friendship;
import com.company.exceptions.ValidationException;

/**
 * FriendshipValidator validates an object of type Friendship
 */
public class FriendshipValidator implements Validator<Friendship> {
    /**
     * Validates a friendship
     * To be valid, friendship id and users ids cannot be null or negative
     * @param entity - the friendship to be validated
     * @throws ValidationException if the vriendship is not valid
     */
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getIdUser1()==null || entity.getIdUser2()==null){
            throw new ValidationException("Friends ids cannot be null");
        }
        if(entity.getIdUser1()<0 || entity.getIdUser2()<0){
            throw new ValidationException("Friends ids cannot be negative");
        }
    }
}
