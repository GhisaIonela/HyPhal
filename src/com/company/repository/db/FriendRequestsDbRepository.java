package com.company.repository.db;

import com.company.domain.FriendRequest;
import com.company.domain.FriendRequestStatus;
import com.company.domain.Friendship;
import com.company.exceptions.ValidationException;
import com.company.repository.Repository;
import com.company.utils.Constants;
import com.company.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FriendRequestsDbRepository manages the CRUD operations for friendship class with database persistence
 */
public class FriendRequestsDbRepository implements Repository<Long, FriendRequest> {
    private String url;
    private String username;
    private String password;
    private Validator<FriendRequest> validator;

    /**
     * Constructs a new FriendRequestsDbRepository
     * @param url - database url
     * @param username - database username
     * @param password - database password
     * @param validator - a validator for Friend requests
     */
    public FriendRequestsDbRepository(String url, String username, String password, Validator<FriendRequest> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    /**
     * Search by id for the friend request
     * @param id -the id of the friend request to be returned
     *            id must not be null
     * @return the friend requests with the specified id
     *         or null - if there is no friend requests with the given id
     * @throws IllegalArgumentException
     *         if the given id is null.
     */
    @Override
    public FriendRequest findOne(Long id) {
        if (id ==null)
            throw new IllegalArgumentException("id must not be null");

        List<FriendRequest> friendRequests =  new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friend_requests WHERE id = ?"))
        {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long idFrom = resultSet.getLong("id_from");
                Long idTo = resultSet.getLong("id_to");
                FriendRequestStatus status = FriendRequestStatus.valueOf(resultSet.getString("status"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all the friend requests from database
     * @return all the friend requests
     */
    @Override
    public Iterable<FriendRequest> findAll() {
        List<FriendRequest> friendRequests = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friend_requests"))
        {
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                Long id_from = resultSet.getLong("id_from");
                Long id_to = resultSet.getLong("id_to");
                FriendRequestStatus status = FriendRequestStatus.valueOf(resultSet.getString("status"));
                FriendRequest friendRequest = new FriendRequest(id_from, id_to);
                friendRequest.setId(id);
                friendRequest.setStatus(status);

                friendRequests.add(friendRequest);
            }
            return friendRequests;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Save the friend request in database
     * @param friendRequest
     *         friend request must be not null
     * @return null- if the given friend request is saved
     *         otherwise returns the friend request (id already exists)
     * @throws ValidationException
     *            if the friend request is not valid
     * @throws IllegalArgumentException
     *             if the given friend request is null.
     */
    @Override
    public FriendRequest save(FriendRequest friendRequest) {
        if (friendRequest==null)
            throw new IllegalArgumentException("friend requet must not be null");
        validator.validate(friendRequest);

        String sql = "insert into friendships (id_from, id_to, status) values (?, ?, ?)";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setLong(1, friendRequest.getidFrom());
            ps.setLong(2, friendRequest.getidTo());
            ps.setString(3, friendRequest.getStatus().name());

            ps.executeUpdate();
            return null;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return friendRequest;
    }

    /**
     * Removes the friend request with the specified id from the database
     * @param id - must be not null
     * @return the removed friend request or null if there is no friend request with the given id
     * @throws IllegalArgumentException
     *         if the given id is null.
     */
    @Override
    public FriendRequest delete(Long id) {
        if(id==null)
            throw new IllegalArgumentException("id must not be null");

        List<FriendRequest> friendRequests= new ArrayList<>();
        String sql = "delete from friend_requests where id = ? returning * ";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setLong(1,id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Long id_from = resultSet.getLong("id_from");
                Long id_to = resultSet.getLong("id_to");
                FriendRequestStatus status = FriendRequestStatus.valueOf(resultSet.getString("status"));
                FriendRequest friendRequest = new FriendRequest(id_from, id_to);
                friendRequest.setId(id);
                friendRequest.setStatus(status);
            }
            if(friendRequests.size()!=0){
                return friendRequests.get(0);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public FriendRequest update(FriendRequest friendRequest) {
        return null;
    }
}