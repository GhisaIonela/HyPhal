package com.company.repository.db;

import com.company.credentials.UserCredentials;
import com.company.domain.User;
import com.company.exceptions.RepositoryDbException;
import com.company.exceptions.ValidationException;
import com.company.repository.Repository;
import com.company.utils.Constants;
import com.company.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
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

        String sql = "SELECT users.id, users.email, users.first_name, users.last_name, users.city, users.date_of_birth, credentials.password\n" +
                "FROM users\n" +
                "INNER JOIN credentials\n" +
                "ON credentials.email = users.email\n" +
                "WHERE users.id = ?";
        User user = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                user = buildUser(resultSet);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    /**
     * Creates a user from data extracted from database table
     * @param resultSet - the resultSet with extracted data
     * @return the user
     * @throws SQLException if an SQL error occurs
     */
    private User buildUser(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        String email = resultSet.getString("email");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String city = resultSet.getString("city");
        LocalDateTime dateOfBirth = LocalDateTime.parse(resultSet.getString("date_of_birth"), Constants.DATE_OF_BIRTH_FORMATTER);

        String password = resultSet.getString("password");

        User user = new User(email, firstName, lastName, city, dateOfBirth, password);
        user.setId(id);
        return user;
    }

    /**
     * Gets all the users from database
     * @return all the users
     */
    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        String sql = "SELECT users.id, users.email, users.first_name, users.last_name, users.city, users.date_of_birth, credentials.password FROM users\n" +
                     "INNER JOIN credentials USING (email)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
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

        String sql = "insert into users (email, first_name, last_name, city, date_of_birth ) values (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getCity());
            ps.setTimestamp(5, Timestamp.valueOf(user.getDateOfBirth().format(Constants.DATE_OF_BIRTH_FORMATTER)));

            ps.executeUpdate();

            String sql2 = "insert into credentials(email, password) values (?,?)";
            try(Connection connection2 = DriverManager.getConnection(url, username, password);
                PreparedStatement ps2 = connection2.prepareStatement(sql2)) {
                ps2.setString(1, user.getEmail());
                ps2.setString(2, user.getUserCredentials().getPassword());
                ps2.executeUpdate();
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
        User user = null;
        if(id==null)
            throw new IllegalArgumentException("id must not be null");

        String sql2 = "SELECT users.id, users.email, users.first_name, users.last_name, users.city, users.date_of_birth, credentials.email\n" +
                "FROM users\n" +
                "INNER JOIN credentials\n" +
                "ON credentials.email = users.email\n" +
                "WHERE users.id = ?";

        try(Connection connection2 = DriverManager.getConnection(url, username, password);
            PreparedStatement ps2 = connection2.prepareStatement(sql2))
        {
            ps2.setLong(1,id);
            ResultSet resultSet2 = ps2.executeQuery();

            if (resultSet2.next()) {
                user = buildUser(resultSet2);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String sql = "delete from users where id = ? returning * ";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setLong(1,id);
                ps.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    public User findUserByEmail(String email){
        String sql = "SELECT users.id, users.email, users.first_name, users.last_name, users.city, users.date_of_birth, credentials.password\n" +
                "FROM users\n" +
                "INNER JOIN credentials\n" +
                "ON credentials.email = users.email\n" +
                "WHERE users.email = ?";
        User user = null;

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, email);
            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                user = buildUser(resultSet);
            }
        }catch  (SQLException throwables){
            throw new RepositoryDbException("User db exception\n" + throwables.getMessage());
        }

        return user;
    }

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
        validator.validate(user);
        User toUpdate = findOne(user.getId());
        if(toUpdate!=null){
            String updateStatement = "UPDATE users SET email=?, first_name=?, last_name=?, city=?, date_of_birth=? WHERE id=?";
            try(Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement ps = connection.prepareStatement(updateStatement))
            {
                    ps.setString(1,user.getEmail());
                    ps.setString(2, user.getFirstName());
                    ps.setString(3, user.getLastName());
                    ps.setString(4,user.getCity());
                    ps.setTimestamp(5, Timestamp.valueOf(user.getDateOfBirth().format(Constants.DATE_OF_BIRTH_FORMATTER)));
                    ps.setLong(6,user.getId());

                    ps.executeUpdate();

                    update(user.getUserCredentials());
                } catch (SQLException throwables) {
                    System.out.println(throwables.getMessage());
                }
            }else
                return user;
        return null;
    }

    /**
     * Update the user's password from the database
     * @param userCredentials - user's credentials
     */
    public void update(UserCredentials userCredentials){
        if (findUserByEmail(userCredentials.getEmail())!=null){
            String sql = "UPDATE credentials SET password = ? WHERE email = ?";
            try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, userCredentials.getPassword());
                ps.setString(2, userCredentials.getEmail());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}

