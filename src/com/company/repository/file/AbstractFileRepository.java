package com.company.repository.file;

import com.company.domain.Entity;
import com.company.repository.memory.InMemoryRepository;
import com.company.validators.Validator;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * AbstractFileRepository manages the CRUD operations with file data persistence
 * @param <ID> - the id for Entity class
 * @param <E> - a class to work with
 */
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    private String fileName;

    /**
     * Sets the name of the file and loads data from that file
     * @param fileName - the name of file
     * @param validator - a validator for E
     */
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    /**
     * Loads the data from the file
     */
    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie=br.readLine())!=null){
                List<String> attr= Arrays.asList(linie.split(";"));
                super.save(extractEntity(attr));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of attributes
     * @param attributes entity's attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);

    /**
     * create entity as string - template method design pattern
     * creates an entity as string from entity's attributes
     * @param entity the entity
     * @return a string representation of entity
     */
    protected abstract String createEntityAsString(E entity);

    /**
     * Save a new entity
     * @param entity - the entity to save
     * @return null- if the given entity is saved
     *         otherwise returns the entity (id already exists)
     */
    @Override
    public E save(E entity){

        if(super.save(entity)==null){
            writeToFile(entity);
            return null;
        }
       return entity;
    }

    /**
     * Delete an entity
     * @param id - the id of the entity to delete
     * @return the removed entity or null if there is no entity with the given id
     */
    @Override
    public E delete(ID id) {
        E deleted = super.delete(id);
        if(deleted!=null){
            clearFile();
            for(E elem:findAll()){
                writeToFile(elem);
            }
            return deleted;
        }
        return null;
    }

    /**
     * Clear the file
     */
    public void clearFile(){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();
    }

    /**
     * Write an entity to the file
     * @param entity - the entity to be written to the file
     */
    protected void writeToFile(E entity){
        try (BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(fileName,true)) ){
            bufferedWriter.write(createEntityAsString(entity));
            bufferedWriter.newLine();
        }
        catch (IOException e) {
            e.printStackTrace();

        }
    }

}

