package com.company.repository.db;

import com.company.domain.User;
import com.company.exceptions.ValidationException;
import com.company.repository.Repository;
import com.company.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * UserDbRepository manages the CRUD operations for User class with database persistence
 */
public class UserDbRepository implements Repository<Long, User> {
    private String url;
    private String username;
    private String password;
    private Validator<User> validator;

    /**
     * Construct a new UserDbRepository
     * @param url - database url
     * @param username - database username
     * @param password - database password
     * @param validator - a validator for Friendships
     */
    public UserDbRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    /**
     * Search by id for the user
     * @param id -the id of the user to be returned
     *            id must not be null
     * @return the user with the specified id
     *         or null - if there is no user with the given id
     * @throws IllegalArgumentException
     *         if the given id is null.
     */
    @Override
    public User findOne(Long id) {
        if (id==null)
            throw new IllegalArgumentException("id must not be null");

        List<User> users= new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id_real = ?"))
             {
                 statement.setLong(1, id);
                 ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                User user = buildUser(resultSet);
                users.add(user);
            }
            if(users.size()!=0){
                return users.get(0);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a user from data extracted from database table
     * @param resultSet - the resultSet with extracted data
     * @return the user
     * @throws SQLException if an SQL error occurs
     */
    private User buildUser(ResultSet resultSet) throws SQLException{
        Long id_real = resultSet.getLong("id_real");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");

        User user = new User(firstName, lastName);
        user.setId(id_real);
        return user;
    }

    /**
     * Gets all the users from database
     * @return all the users
     */
    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = buildUser(resultSet);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Save the user in database
     * @param user
     *         user must be not null
     * @return null- if the given user is saved
     *         otherwise returns the user (id already exists)
     * @throws ValidationException
     *            if the user is not valid
     * @throws IllegalArgumentException
     *             if the given user is null.
     */
    @Override
    public User save(User user) {
        if (user==null)
            throw new IllegalArgumentException("user must not be null");
        validator.validate(user);

        String sql = "insert into users (id_real, first_name, last_name ) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, user.getId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());

            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Removes the user with the specified id from the database
     * @param id
     *      id must be not null
     * @return the removed user or null if there is no user with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    @Override
    public User delete(Long id) {
        if(id==null)
            throw new IllegalArgumentException("id must not be null");

        List<User> users= new ArrayList<>();
        String sql = "delete from users where id_real = ? returning * ";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setLong(1,id);
                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()) {
                    User user = buildUser(resultSet);
                    users.add(user);
            }
            if(users.size()!=0){
                return users.get(0);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    //to be continued
    /**
     * Update the user from the databse
     * @param user
     *          user must not be null
     * @return null - if the user is updated,
     *                otherwise  returns the user  - (e.g id does not exist).
     * @throws IllegalArgumentException
     *             if the given user is null.
     * @throws ValidationException
     *             if the user is not valid.
     */
    @Override
    public User update(User user) {
        return null;
    } //TO DO
}

