package com.company.repository.file;

import com.company.domain.Friendship;
import com.company.validators.Validator;

import java.util.List;

/**
 * FriendshipFile manages the CRUD operations with file data persistence for User class
 */
public class FriendshipFile extends AbstractFileRepository<Long, Friendship> {

    /**
     * Constructs a new FriendshipFile with a file name and a validator
     * @param fileName - the name of the file
     * @param validator - the validator for Friendship class
     */
    public FriendshipFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    /**
     * Creates an entity of type Friendship having a specified list of attributes
     * @param attributes friendship's attributes
     * @return the friendship
     */
    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship friendship = new Friendship(Long.parseLong(attributes.get(1)), Long.parseLong(attributes.get(2)));
        friendship.setId(Long.parseLong(attributes.get(0)));
        return friendship;
    }

    /**
     * Creates a friendship as string from friendship's attributes
     * @param entity - the entity
     * @return a string representation of friendship
     */
    @Override
    protected String createEntityAsString(Friendship entity) {
        return Long.toString(entity.getId()) + ';' + Long.toString(entity.getIdUser1()) + ';' + Long.toString(entity.getIdUser2());
    }
}
