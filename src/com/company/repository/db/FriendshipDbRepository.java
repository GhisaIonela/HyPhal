package com.company.repository.db;

import com.company.utils.Constants;
import com.company.domain.Friendship;
import com.company.exceptions.ValidationException;
import com.company.repository.Repository;
import com.company.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * FriendshipDbRepository manages the CRUD operations for Friendship class with database persistence
 */
public class FriendshipDbRepository implements Repository<Long, Friendship> {
    private String url;
    private String username;
    private String password;
    private Validator<Friendship> validator;

    /**
     * Constructs a new FriendshipDbRepository
     * @param url - database url
     * @param username - database username
     * @param password - database password
     * @param validator - a validator for Friendships
     */
    public FriendshipDbRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    /**
     * Search by id for the friendships
     * @param id -the id of the friendships to be returned
     *            id must not be null
     * @return the friendships with the specified id
     *         or null - if there is no friendships with the given id
     * @throws IllegalArgumentException
     *         if the given id is null.
     */
    @Override
    public Friendship findOne(Long id) {
        if (id==null)
            throw new IllegalArgumentException("id must not be null");

        List<Friendship> friendships = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships WHERE id_real = ?"))
        {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Long id_real = resultSet.getLong("id_real");
                Long id_user1 = resultSet.getLong("id_user1");
                Long id_user2 = resultSet.getLong("id_user2");
                LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("date_time"), Constants.DATE_TIME_FORMATTER);

                Friendship friendship = new Friendship(id_user1, id_user2, dateTime);
                friendship.setId(id_real);
                friendships.add(friendship);

            }
            if(friendships.size()!=0){
                return friendships.get(0);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a friendship from data extracted from database table
     * @param resultSet - the resultSet with extracted data
     * @return the friendship
     * @throws SQLException if an SQL error occurs
     */
    private Friendship buildFriendship(ResultSet resultSet) throws SQLException {
        Long id_real = resultSet.getLong("id_real");
        Long id_user1 = resultSet.getLong("id_user1");
        Long id_user2 = resultSet.getLong("id_user2");
        LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("date_time"), Constants.DATE_TIME_FORMATTER);

        Friendship friendship = new Friendship(id_user1, id_user2, dateTime);
        friendship.setId(id_real);
        return friendship;
    }

    /**
     * Gets all the friendships from database
     * @return all the friendships
     */
    @Override
    public Iterable<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships"))
        {
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Friendship friendship = buildFriendship(resultSet);
                friendships.add(friendship);
            }
            return friendships;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Save the friendship in database
     * @param friendship
     *         friendship must be not null
     * @return null- if the given friendship is saved
     *         otherwise returns the friendship (id already exists)
     * @throws ValidationException
     *            if the friendship is not valid
     * @throws IllegalArgumentException
     *             if the given friendship is null.
     */
    @Override
    public Friendship save(Friendship friendship) {
        if (friendship==null)
            throw new IllegalArgumentException("friendship must not be null");
        validator.validate(friendship);

        String sql = "insert into friendships (id_real, id_user1, id_user2) values (?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setLong(1, friendship.getId());
            ps.setLong(2, friendship.getIdUser1());
            ps.setLong(3, friendship.getIdUser2());
            ps.setTimestamp(3, Timestamp.valueOf(friendship.getDateTime().format(Constants.DATE_TIME_FORMATTER)));

            ps.executeUpdate();
            return null;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return friendship;
    }

    /**
     * Removes the friendship with the specified id from the database
     * @param id
     *      id must be not null
     * @return the removed friendship or null if there is no friendship with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    @Override
    public Friendship delete(Long id) {
        if(id==null)
            throw new IllegalArgumentException("id must not be null");

        List<Friendship> friendships= new ArrayList<>();
        String sql = "delete from friendships where id_real = ? returning * ";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setLong(1,id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Friendship friendship = buildFriendship(resultSet);
                friendships.add(friendship);
            }
            if(friendships.size()!=0){
                return friendships.get(0);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Update the friendship from the databse
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
    public Friendship update(Friendship entity) {
        return null;
    } //TO DO
}
