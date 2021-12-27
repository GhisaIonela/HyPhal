package com.company.controller;

import com.company.domain.*;
import com.company.dto.ConversationDTO;
import com.company.dto.FriendRequestDTO;
import com.company.dto.UserFriendshipDTO;
import com.company.exceptions.ServiceException;
import com.company.exceptions.UserNotFoundException;
import com.company.service.*;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
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
    private LoginManager loginManager;
    private MessageService messageService;
    private FriendRequestService friendRequestService;

    /**
     * Contruscts a new Controller
     * @param userService - the service for the User repository
     * @param friendshipService - the service for the User repository
     * @param network - the network
     */
    public Controller(UserService userService, FriendshipService friendshipService, Network network, LoginManager loginManager, MessageService messageService, FriendRequestService friendRequestService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.network = network;
        network.loadNetwork();
        this.loginManager = loginManager;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
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

    public void updatePassword(String email, String password){
        userService.updatePassword(email, password);
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

    /**
     * Finds a friendship between two users
     * @param user1 first user
     * @param user2 second user
     * @return - the friendship if found
     *         - null if not found
     */
    public Friendship findFriendShip(User user1, User user2) {
        return friendshipService.findAny(user1.getId(), user2.getId());
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
                    return new UserFriendshipDTO(friend.getId(), friend.getFirstName(), friend.getLastName(), friendship.getDateTime());
                })
                .collect(Collectors.toList());
    }

    public List<UserFriendshipDTO> findUserFriendships(User user){
        return  StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.getIdUser1().equals(user.getId()) || friendship.getIdUser2().equals(user.getId()))
                .map(friendship -> {
                    User friend = userService.getFriend(user, friendship);
                    return new UserFriendshipDTO(friend.getId(), friend.getFirstName(), friend.getLastName(), friendship.getDateTime());
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
                    return new UserFriendshipDTO(friend.getId(), friend.getFirstName(), friend.getLastName(), friendship.getDateTime());
                })
                .collect(Collectors.toList());
    }

    public Iterable<UserFriendshipDTO> findUserFriendshipsByMonth(User user, Month month) {
        return  StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> (friendship.getIdUser1().equals(user.getId()) || friendship.getIdUser2().equals(user.getId()))
                        && friendship.getDateTime().getMonth() == month)
                .map(friendship -> {
                    User friend = userService.getFriend(user, friendship);
                    return new UserFriendshipDTO(friend.getId(), friend.getFirstName(), friend.getLastName(), friendship.getDateTime());
                })
                .collect(Collectors.toList());
    }

    public ConversationManager createConversation(String email){
        User sender = loginManager.getLogged();
        User receiver = userService.findUserByEmail(email);
        ConversationManager conversation = new ConversationManager(messageService, sender, receiver);
        return conversation;
    }

    public void sendMessageToMultipleUsers(List<String> emails, String message){
        List<User> receivers = new ArrayList<>();
        Predicate<String> isNotNull = email -> userService.findUserByEmail(email) != null;
        emails.stream().filter(isNotNull)
                       .forEach(email->receivers.add(userService.findUserByEmail(email)));
        messageService.save(loginManager.getLogged(), receivers, message, null);
    }

    public Iterable<ConversationDTO> getConversationsInfo(){
        List<ConversationDTO> conversationsInfos = new ArrayList<>();
        userService.findAll().forEach(user -> conversationsInfos.add(new ConversationDTO(user.getFirstName(), user.getLastName(), user.getEmail())));
        return conversationsInfos;
    }

    public Iterable<Message> GetSortedMessagesBetweenTwoUsersByDate(String email1, String email2){
        Long idUser1 = userService.findUserByEmailId(email1);
        Long idUser2 = userService.findUserByEmailId(email2);
        return messageService.getSortedMessagesByDateTwoUsers(idUser1, idUser2);
    }

    public List<FriendRequestDTO> findReceivedUserFriendRequests(String email){
        Long idUser = userService.findUserByEmailId(email);
        return StreamSupport.stream(friendRequestService.findAll().spliterator(), false)
                .filter(friendRequest -> friendRequest.getIdTo().equals(idUser) &&  friendRequest.getStatus().equals(FriendRequestStatus.pending))
                .map(friendRequest -> {
                    User from = userService.findOne(friendRequest.getIdFrom());
                    User to = userService.findOne(friendRequest.getIdTo());
                    return new FriendRequestDTO(from.getId(), from.getFirstName(), from.getLastName(), from.getEmail(),
                                                to.getId(), to.getFirstName(), to.getLastName(), to.getEmail());
                })
                .collect(Collectors.toList());
    }

    public List<FriendRequestDTO> findReceivedUserFriendRequests(User user){
        return StreamSupport.stream(friendRequestService.findAll().spliterator(), false)
                .filter(friendRequest -> friendRequest.getIdTo().equals(user.getId()) &&  friendRequest.getStatus().equals(FriendRequestStatus.pending))
                .map(friendRequest -> {
                    User from = userService.findOne(friendRequest.getIdFrom());
                    User to = userService.findOne(friendRequest.getIdTo());
                    return new FriendRequestDTO(from.getId(), from.getFirstName(), from.getLastName(), from.getEmail(),
                            to.getId(), to.getFirstName(), to.getLastName(), to.getEmail());
                })
                .collect(Collectors.toList());
    }

    public List<FriendRequestDTO> findSentUserFriendRequests(String email){
        Long idUser = userService.findUserByEmailId(email);
        return StreamSupport.stream(friendRequestService.findAll().spliterator(), false)
                .filter(friendRequest -> friendRequest.getIdFrom().equals(idUser) && friendRequest.getStatus().equals(FriendRequestStatus.pending))
                .map(friendRequest -> {
                    User from = userService.findOne(friendRequest.getIdFrom());
                    User to = userService.findOne(friendRequest.getIdTo());
                    return new FriendRequestDTO(from.getId(), from.getFirstName(), from.getLastName(), from.getEmail(),
                            to.getId(), to.getFirstName(), to.getLastName(), to.getEmail());
                })
                .collect(Collectors.toList());
    }

    public List<FriendRequestDTO> findSentUserFriendRequests(User user){
        Long id = user.getId();
        return StreamSupport.stream(friendRequestService.findAll().spliterator(), false)
                .filter(friendRequest -> friendRequest.getIdFrom().equals(user.getId()) && friendRequest.getStatus().equals(FriendRequestStatus.pending))
                .map(friendRequest -> {
                    User from = userService.findOne(friendRequest.getIdFrom());
                    User to = userService.findOne(friendRequest.getIdTo());
                    return new FriendRequestDTO(from.getId(), from.getFirstName(), from.getLastName(), from.getEmail(),
                            to.getId(), to.getFirstName(), to.getLastName(), to.getEmail());
                })
                .collect(Collectors.toList());
    }

    public List<FriendRequestDTO> findAllUserFriendRequests(String email) {
        Long userId = userService.findUserByEmailId(email);
        return StreamSupport.stream(friendRequestService.findAll().spliterator(), false)
                .filter(friendRequest -> (friendRequest.getIdFrom().equals(userId) || friendRequest.getIdTo().equals(userId) && friendRequest.getStatus().equals(FriendRequestStatus.pending)))
                .map(friendRequest -> {
                    User from = userService.findOne(friendRequest.getIdFrom());
                    User to = userService.findOne(friendRequest.getIdTo());
                    return new FriendRequestDTO(from.getId(), from.getFirstName(), from.getLastName(), from.getEmail(),
                            to.getId(), to.getFirstName(), to.getLastName(), to.getEmail());
                })
                .collect(Collectors.toList());
    }

    public List<FriendRequestDTO> findAllUserFriendRequests(User user) {
        return StreamSupport.stream(friendRequestService.findAll().spliterator(), false)
                .filter(friendRequest -> (friendRequest.getIdFrom().equals(user.getId()) || friendRequest.getIdTo().equals(user.getId()) && friendRequest.getStatus().equals(FriendRequestStatus.pending)))
                .map(friendRequest -> {
                    User from = userService.findOne(friendRequest.getIdFrom());
                    User to = userService.findOne(friendRequest.getIdTo());
                    return new FriendRequestDTO(from.getId(), from.getFirstName(), from.getLastName(), from.getEmail(),
                            to.getId(), to.getFirstName(), to.getLastName(), to.getEmail());
                })
                .collect(Collectors.toList());
    }


    public Iterable<FriendRequest> findAllFriendRequests() { return friendRequestService.findAll(); }

    public FriendRequest findFriendRequest(User user1, User user2) {
        return friendRequestService.findFriendRequest(user1.getId(), user2.getId());
    }

    public FriendRequest sendFriendRequest (String fromEmail, String toEmail){
        Long idFrom = userService.findUserByEmailId(fromEmail);
        Long idTo = userService.findUserByEmailId(toEmail);
        return friendRequestService.sendFriendRequest(idFrom, idTo);
    }

    public FriendRequest sendFriendRequest (User from, User to){
        return friendRequestService.sendFriendRequest(from.getId(), to.getId());
    }

    public FriendRequest sendFriendRequestAndReturn(User from, User to) {
        return friendRequestService.sendFriendRequestAndReturn(from.getId(), to.getId());
    }

    public FriendRequest cancelFriendRequest(String fromEmail, String toEmail){
        Long idFrom = userService.findUserByEmailId(fromEmail);
        Long idTo = userService.findUserByEmailId(toEmail);
        return friendRequestService.cancelFriendRequest(idFrom, idTo);
    }

    public FriendRequest cancelFriendRequest(User from, User to){
        return friendRequestService.cancelFriendRequest(from.getId(), to.getId());
    }

    public FriendRequest acceptFriendRequest(String fromEmail, String toEmail){
        Long idFrom = userService.findUserByEmailId(fromEmail);
        Long idTo = userService.findUserByEmailId(toEmail);
        return friendRequestService.acceptFriendRequest(idFrom, idTo);
    }

    public FriendRequest acceptFriendRequest(User from, User to){
        return friendRequestService.acceptFriendRequest(from.getId(), to.getId());
    }

    public Friendship acceptFriendRequestAndReturnFriendship(User from, User to){
        return friendRequestService.acceptFriendRequestAndReturnFriendShip(from.getId(), to.getId());
    }

    public FriendRequest denyFriendRequest(String fromEmail, String toEmail){
        Long idFrom = userService.findUserByEmailId(fromEmail);
        Long idTo = userService.findUserByEmailId(toEmail);
        return friendRequestService.denyFriendRequest(idFrom, idTo);
    }

    public FriendRequest denyFriendRequest(User from, User to){
        return friendRequestService.denyFriendRequest(from.getId(), to.getId());
    }
    //endregion

    //region Login

    /**
     * Login a user
     * @param email - user's email
     * @param password - user's
     */
    public void login(String email, String password){
            loginManager.login(email, password);
    }

    /**
     * Verify if a user is logged
     * @return true if is logged, false otherwise
     */
    public boolean isLogged(){
        return loginManager.isLogged();
    }

    /**
     * Logs out a user
     */
    public void logOut(){
        loginManager.logOut();
    }

    /**
     * Create a new user account
     * @param email     - user's email
     * @param firstName - user's firstName
     * @param lastName  - user's lastName
     * @param city      - user's city
     * @param dateOfBirth - user's date of birth
     * @return null- if the given user is saved
     * otherwise returns the user (id user exists)
     */
    public User createAccount(String email, String firstName, String lastName, String city, LocalDateTime dateOfBirth, String password){
        return saveUser(email, firstName, lastName, city, dateOfBirth, password);
    }

    //endregion

}
