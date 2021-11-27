package com.company.service;

import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.exceptions.ValidationException;
import com.company.repository.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * UserService is a service for User class
 */
public class UserService {
    private Repository<Long, User> userRepository;
    private Repository<Long, Friendship> friendshipRepository;

    /**
     * Constructs a new UserService
     * @param friendshipRepository - the repository for Friendship class
     * @param userRepository - the repository for User class
     */
    public UserService(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        connectUsersWithFriends();
    }

    /**
     * Search by id for a user
     * @param id - the user's id
     * @return the user with the specified id
     *          or null - if there is no user with the given id
     */
    public User findOne(Long id){
        try{
            return userRepository.findOne(id);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Gets all users
     * @return all users
     */
    public Iterable<User> findAll(){
        return userRepository.findAll();
    }

    /**
     * Save a new user
     * @param id - the user's id
     * @param firstName - user's firstName
     * @param lastName - user's lastName
     * @return null- if the given user is saved
     *         otherwise returns the user (id user exists)
     */
    public User save(Long id, String firstName, String lastName){
        try{
            User UserToSave = new User(firstName, lastName);
            UserToSave.setId(id);
            return userRepository.save(UserToSave);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        catch (ValidationException v){
            System.out.println(v.getMessage());
        }
        return null;
    }

    /**
     * Delete a user
     * @param id - the user's id
     * @return the removed user or null if there is no user with the given id
     */
    public User delete(Long id){
        try{
            User deleted = userRepository.delete(id);
            if(deleted!=null){
                List<Friendship> friendships = new ArrayList<>();
                friendshipRepository.findAll().forEach(friendships::add);
                for(Friendship friendship: friendships){
                    if (friendship.getIdUser1().equals(id) || friendship.getIdUser2().equals(id))
                        friendshipRepository.delete(friendship.getId());
                }
                connectUsersWithFriends();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Update a new user
     * @param id - the user's id
     * @param firstName - user's firstName
     * @param lastName - user's lastName
     * @return null- if the given user is updated
     *         otherwise returns the user (id user exists)
     */
    public User update(Long id, String firstName, String lastName){

        //to be continued

        try{
            User updatedUser = new User(firstName, lastName);
            updatedUser.setId(id);
            User updated =  userRepository.update(updatedUser);
            if(updated==null){
                connectUsersWithFriends();
            }
            return updated;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ValidationException v){
            System.out.println(v.getMessage());
        }
        return null;
    }

    /**
     * Determine if a user is in a friendship
     * @param user - a user
     * @param friendship - a friendship
     * @return true if the user is in friendship, false otherwise
     */
    public boolean isInFriendship(User user, Friendship friendship){
        return user.getId() == friendship.getIdUser1() || user.getId() == friendship.getIdUser2();
    }

    /**
     * Get the user's friend from a certain friendship
     * @param user - the user
     * @param friendship - the friendship in which the user takes part
     * @return the user's friend or null if the user is not in the friendship
     */
    public User getFriend(User user, Friendship friendship){
        if(isInFriendship(user, friendship)){
            if(user.getId()!=friendship.getIdUser1())
                return findOne(friendship.getIdUser1());
            return findOne(friendship.getIdUser2());
        }
        return null;
    }

    /**
     * Connect users with theirs friends
     * Add the friends in the user's friends list
     */
    public void connectUsersWithFriends() {
        for (User user : findAll()) {
            if(findAll()!=null){
                List<User> userFriends = new ArrayList<>();
                if(friendshipRepository.findAll()!=null)
                {
                    for (Friendship friendship : friendshipRepository.findAll()) {
                        if(friendshipRepository.findAll()!=null){
                            User friend = getFriend(user, friendship);
                            if(friend!=null){
                                User friendCopy = new User(friend.getFirstName(), friend.getLastName());
                                friendCopy.setId(friend.getId());
                                userFriends.add(friendCopy);
                            }
                        }
                    }
                    user.setFriends(userFriends);
                }

            }
        }
    }
}

