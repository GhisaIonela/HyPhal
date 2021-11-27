package com.company.repository.memory;

import com.company.domain.Entity;
import com.company.exceptions.ValidationException;
import com.company.repository.Repository;
import com.company.validators.Validator;

import java.util.HashMap;
import java.util.Map;

/**
 * InMemoryRepository manages CRUD operations with memory data persistence
 * @param <ID> - the id for Entity class
 * @param <E> - a class to work with
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private Validator<E> validator;
    Map<ID,E> entities;

    /**
     * Constructs a new InMemoryRepository
     * @param validator - the validator for E
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID,E>();
    }

    /**
     * Search by id for the object of type E
     * @param id -the id of the entity to be returned
     *            id must not be null
     * @return the entity with the specified id
     *         or null - if there is no entity with the given id
     * @throws IllegalArgumentException
     *         if the given id is null.
     */
    @Override
    public E findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must not be null");
        return entities.get(id);
    }

    /**
     * Gets all the entities
     * @return all entities
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     * Save the entity
     * @param entity
     *         entity must be not null
     * @return null- if the given entity is saved
     *         otherwise returns the entity (id already exists)
     * @throws ValidationException
     *            if the entity is not valid
     * @throws IllegalArgumentException
     *             if the given entity is null.
     */
    @Override
    public E save(E entity) throws ValidationException {
        if (entity==null)
            throw new IllegalArgumentException("entity must not be null");
        validator.validate(entity);
        if(findOne(entity.getId())!=null) {
            return entity;
        }
        else entities.put(entity.getId(),entity);
        return null;
    }


    /**
     * Removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    @Override
    public E delete(ID id) {
        if(id==null)
            throw new IllegalArgumentException("id must not be null");
        else if(entities.containsKey(id))
            return entities.remove(id);
        return null;
    }

    /**
     * Update the entity
     * @param entity
     *          entity must not be null
     * @return null - if the entity is updated,
     *                otherwise  returns the entity  - (e.g id does not exist).
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidationException
     *             if the entity is not valid.
     */
    @Override
    public E update(E entity) throws ValidationException{

        if(entity == null)
            throw new IllegalArgumentException("entity must not be null!");
        validator.validate(entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;
    }

}
