package com.company.repository.db;

import com.company.domain.FriendRequest;
import com.company.domain.FriendRequestStatus;
import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.exceptions.ValidationException;
import com.company.repository.Repository;
import com.company.utils.Constants;
import com.company.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * FriendRequestsDbRepository manages the CRUD operations for friendship class with database persistence
 */
public class FriendRequestsDbRepository implements Repository<Long, FriendRequest> {
    private Connection connection;

    /**
     * Constructs a new FriendRequestsDbRepository

     */
    public FriendRequestsDbRepository(Connection connection) {
       this.connection = connection;
    }

    private FriendRequest buildFriendRequest(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long idFrom = resultSet.getLong("id_from");
        Long idTo = resultSet.getLong("id_to");
        FriendRequestStatus status = FriendRequestStatus.valueOf(resultSet.getString("status"));
        LocalDateTime date_time = resultSet.getTimestamp("date_time").toLocalDateTime();

        FriendRequest friendRequest = new FriendRequest(idFrom, idTo, status, date_time);
        friendRequest.setId(id);
        return friendRequest;
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
        try (
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friend_requests WHERE id = ?"))
        {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {;
                friendRequests.add(buildFriendRequest(resultSet));
            }
            if(friendRequests.size()!=0){
                return friendRequests.get(0);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Search by the ids of the users for the friend request
     * @param idFrom -the id of the user who sends the friend request
     *                must not be null
     * @param idTo - the id of the user who receives the friend request
     *             - must not be null
     * @return the friend requests with the specified user ids
     *         or null - if there is no friend requests with the given ids
     * @throws IllegalArgumentException
     *         if the given ids are null.
     */
    public FriendRequest findOne(Long idFrom, Long idTo) {
        if (idFrom == null || idTo ==null)
            throw new IllegalArgumentException("ids must not be null");

        List<FriendRequest> friendRequests =  new ArrayList<>();
        try (
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friend_requests WHERE id_from = ? AND id_to = ?"))
        {
            statement.setLong(1, idFrom);
            statement.setLong(2, idTo);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                friendRequests.add(buildFriendRequest(resultSet));
            }
            if(friendRequests.size()!=0){
                return friendRequests.get(0);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Search by the user's id for the friend request, with no set order for ids
     * @param idUser1 - the id of the first user
     *                - must not be null
     * @param idUser2 - the id of the second user
     *                must not be null
     * @return the friend request with the specified user id's
     *         or null if there is no friend request with the given user ids
     * @throws IllegalArgumentException if the given user ids are null
     */
    public FriendRequest findAny(Long idUser1, Long idUser2){
        if (idUser1==null || idUser2==null)
            throw new IllegalArgumentException("user ids must not be null");

        List<FriendRequest> friendRequests = new ArrayList<>();
        try (
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM friend_requests WHERE (id_user1 = ? and id_user2 = ?) OR (id_user1 = ? and id_user2 = ?)"))
        {
            statement.setLong(1, idUser1);
            statement.setLong(2, idUser2);
            statement.setLong(3, idUser2);
            statement.setLong(4, idUser1);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                friendRequests.add(buildFriendRequest(resultSet));
            }
            if(friendRequests.size()!=0){
                return friendRequests.get(0);
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
        try (
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friend_requests"))
        {
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                friendRequests.add(buildFriendRequest(resultSet));
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
            throw new IllegalArgumentException("friend request must not be null");

        String sql = "insert into friend_requests (id_from, id_to, status, date_time) values (?, ?, ?, ?)";

        try(
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setLong(1, friendRequest.getIdFrom());
            ps.setLong(2, friendRequest.getIdTo());
            ps.setString(3, friendRequest.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(friendRequest.getDateTime()));

            ps.executeUpdate();
            return null;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return friendRequest;
    }

    /**
     * Save the friend request in database
     * @param friendRequest
     *         friend request must be not null
     * @return friendRequest if saved
     *         null if it already exists
     * @throws ValidationException
     *            if the friend request is not valid
     * @throws IllegalArgumentException
     *             if the given friend request is null.
     */

    public FriendRequest saveAndReturn(FriendRequest friendRequest) {
        if (friendRequest==null)
            throw new IllegalArgumentException("friend request must not be null");

        String sql = "INSERT INTO friend_requests (id_from, id_to, status, date_time) VALUES (?, ?, ?, ?) RETURNING *";

        List<FriendRequest> friendRequests = new ArrayList<>();
        try(
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setLong(1, friendRequest.getIdFrom());
            ps.setLong(2, friendRequest.getIdTo());
            ps.setString(3, friendRequest.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(friendRequest.getDateTime()));

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                friendRequests.add(buildFriendRequest(resultSet));
            }
            if(friendRequests.size()!=0){
                return friendRequests.get(0);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
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
        try(
            PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setLong(1,id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                friendRequests.add(buildFriendRequest(resultSet));
            }
            if(friendRequests.size()!=0){
                return friendRequests.get(0);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the friend request from database
     * @param friendRequest - the request to be updated
     * @return null - if the friend request is updated,
     *                otherwise  returns the friend request  - (e.g id does not exist).
     * @throws IllegalArgumentException
     *             if the given friend request is null.
     */
    @Override
    public FriendRequest update(FriendRequest friendRequest) {
        FriendRequest toUpdate = findOne(friendRequest.getId());
        if(toUpdate!=null){
            String updateStatement = "UPDATE friend_requests SET id_from = ?, id_to = ?, status = ?, date_time = ? WHERE id = ?";
            try(
                PreparedStatement ps = connection.prepareStatement(updateStatement))
            {
                ps.setLong(1, friendRequest.getIdFrom());
                ps.setLong(2, friendRequest.getIdTo());
                ps.setString(3,friendRequest.getStatus().name());
                ps.setTimestamp(4, Timestamp.valueOf(friendRequest.getDateTime()));
                ps.setLong(5, friendRequest.getId());

                ps.executeUpdate();

            } catch (SQLException throwables) {
                System.out.println(throwables.getMessage());
            }
        }else
            return friendRequest;
        return null;
    }
}
