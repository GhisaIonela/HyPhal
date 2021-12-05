package com.company.controller;

import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.dto.UserFriendshipDTO;
import com.company.exceptions.ServiceException;
import com.company.exceptions.UserNotFoundException;
import com.company.service.FriendshipService;
import com.company.service.Network;
import com.company.service.UserService;
import com.company.utils.Constants;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Controller is a class that manages the services of the repositories and the network
 */
public class Controller {
    private UserService userService;
    private FriendshipService friendshipService;
    private Network network;

    /**
     * Contruscts a new Controller
     * @param userService - the service for the User repository
     * @param friendshipService - the service for the User repository
     * @param network - the network
     */
    public Controller(UserService userService, FriendshipService friendshipService, Network network) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.network = network;
        network.loadNetwork();
    }

    //region UserService CRUD
    /**
     * Search by id for a user
     *
     * @param id - the user's id
     * @return the user with the specified id
     * or null - if there is no user with the given id
     */
    public User findUserById(Long id){
        return userService.findOne(id);
    }

    /**
     * Returns the id of the user with the given email address.
     *
     * @param email - user's email
     * @return ID - the corresponding user id
     * @throws UserNotFoundException if the user with the given email does not exist
     */
    public User findUserByEmail(String email){
        return userService.findUserByEmail(email);
    }

    /**
     * Gets all users
     *
     * @return all users
     */
    public Iterable<User> findAllUsers(){
        return userService.findAll();
    }

    /**
     * Save a new user
     *
     * @param email     - user's email
     * @param firstName - user's firstName
     * @param lastName  - user's lastName
     * @param city      - user's city
     * @param dateOfBirth - user's date of birth
     * @return null- if the given user is saved
     * otherwise returns the user (id user exists)
     */
    public User saveUser(String email, String firstName, String lastName, String city, LocalDateTime dateOfBirth, String password){
        return userService.save(email, firstName, lastName, city, dateOfBirth, password);
    }

    /**
     * Delete a user
     *
     * @param email - the user's email
     * @return the deleted user or null if there is no user with the given email
     */
    public User deleteUser(String email){
        return userService.delete(email);
    }

    /**
     * Update a new user
     *
     * @param oldEmail  - the user's old email
     * @param newEmail  - the user's new email
     * @param firstName - user's firstName
     * @param lastName  - user's lastName
     * @param city      - user's city
     * @param dateOfBirth - user's date of birth
     * @return null- if the given user is updated
     * otherwise returns the user (id user exists)
     */
    public User updateUser(String oldEmail, String newEmail, String firstName, String lastName, String city, LocalDateTime dateOfBirth, String password){
        return userService.update(oldEmail, newEmail, firstName, lastName, city, dateOfBirth, password);
    }
    //endregion

    //region FriendshipService CRUD
    /**
     * Search by id for a friendship
     * @param id - the friendship's id
     * @return the friendship with the specified id
     *          or null - if there is no friendship with the given id
     */
    public Friendship findFriendship(Long id){
        return friendshipService.findOne(id);
    }

    /**
     * Gets all friendships
     * @return all friendships
     */
    public Iterable<Friendship> findAllFriendships(){
        return friendshipService.findAll();
    }

    /**
     * Save a new friendship
     * @param idUser1 - first user's id
     * @param idUser2 - second user's id
     * @return null- if the given friendship is saved
     *         otherwise returns the friendship (id friendship exists)
     * @throws ServiceException if a friendship between this two users already exists
     */
    public Friendship saveFriendship(Long idUser1, Long idUser2){
        return friendshipService.save(idUser1, idUser2);
    }

    /**
     * Delete a friendship
     * @param id - the friendship's id
     * @return the removed friendship or null if there is no friendship with the given id
     */
    public Friendship deleteFriendship(Long id){
        return friendshipService.delete(id);
    }
    //endregion

    //region Network
    /**
     * Get the connected(convex) components from the network's graph
     * @return a list of all connected components
     */
    public int getNumberOfConnectedComponents(){
        return network.getNumberOfConnectedComponents();
    }

    /**
     * Count the number of connected components in the network's graph
     * @return the number of connected components
     */
    public List<Long> getMostSociableCommunity(){
        return network.getMostSociableCommunity();
    }
    //endregion

    //region features
    /**
     * Gets the friendships of the user, user that is found by the given email
     * @param email - user's email
     * @return the friends of the user, user that is found by the given email
     */
    public List<UserFriendshipDTO> findUserFriendships(String email){
        User user = userService.findUserByEmail(email);
        return  StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.getIdUser1().equals(user.getId()) || friendship.getIdUser2().equals(user.getId()))
                .map(friendship -> {
                    User friend = userService.getFriend(user, friendship);
                    return new UserFriendshipDTO(friend.getFirstName(), friend.getLastName(), friend.getDateOfBirth());
                })
                .collect(Collectors.toList());
    }

    /**
     * Gets the friendships of the user, user that is found by the given email,
     * that were made in the given month
     * @param email - user's email
     * @param month - the month in which the friendship was made
     * @return the friendships of the user, user that is found by the given email,
     * that were made in the given month
     */
    public Iterable<UserFriendshipDTO> findUserFriendshipsByMonth(String email, Month month) {
        User user = userService.findUserByEmail(email);

        return  StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> (friendship.getIdUser1().equals(user.getId()) || friendship.getIdUser2().equals(user.getId()))
                                        && friendship.getDateTime().getMonth() == month)
                .map(friendship -> {
                    User friend = userService.getFriend(user, friendship);
                    return new UserFriendshipDTO(friend.getFirstName(), friend.getLastName(), friend.getDateOfBirth());
                })
                .collect(Collectors.toList());
    }
    //endregion
}
