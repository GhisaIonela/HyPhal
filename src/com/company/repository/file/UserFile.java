package com.company.repository.file;

import com.company.domain.User;
import com.company.validators.Validator;

import java.util.List;

/**
 * UserFile manages the CRUD operations with file data persistence for User class
 */
public class UserFile extends AbstractFileRepository<Long, User> {

    /**
     * Constructs a new UserFile with a file name and a validator
     * @param fileName - the name of the file
     * @param validator - the validator for User class
     */
    public UserFile(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }

    /**
     * Creates an entity of type User having a specified list of attributes
     * @param attributes user's attributes
     * @return the user
     */
    @Override
    public User extractEntity(List<String> attributes) {

        User user = new User(attributes.get(1), attributes.get(2));
        user.setId(Long.parseLong(attributes.get(0)));
        return user;
    }

    /**
     * Creates a user as string from user's attributes
     * @param entity - the entity
     * @return a string representation of user
     */
    @Override
    protected String createEntityAsString(User entity) {
        return Long.toString(entity.getId())+ ';' + entity.getFirstName() + ';' + entity.getLastName();
    }
}
