package com.company.service;

import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.exceptions.UserNotFoundException;
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
     *
     * @param friendshipRepository - the repository for Friendship class
     * @param userRepository       - the repository for User class
     */
    public UserService(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    /**
     * Search by id for a user
     *
     * @param id - the user's id
     * @return the user with the specified id
     * or null - if there is no user with the given id
     */
    public User findOne(Long id) {
        try {
            return userRepository.findOne(id);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Gets all users
     *
     * @return all users
     */
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Save a new user
     *
     * @param email     - user's email
     * @param firstName - user's firstName
     * @param lastName  - user's lastName
     * @param city      - user's city
     * @return null- if the given user is saved
     * otherwise returns the user (id user exists)
     */
    public User save(String email, String firstName, String lastName, String city) {
        try {
            User UserToSave = new User(email, firstName, lastName, city);
            return userRepository.save(UserToSave);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ValidationException v) {
            System.out.println(v.getMessage());
        }
        return null;
    }

    /**
     * Returns the id of the user with the given email address.
     *
     * @param email - user's email
     * @return ID - the corresponding user id
     * @throws UserNotFoundException if the user with the given email does not exist
     */

    public Long findUserByEmailId(String email) {
        Iterable<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user.getId();
            }
        }
        return null;
    }

    public User findUserByEmail(String email){
        User user = userRepository.findUserByEmail(email);

        if(user == null){
            throw new UserNotFoundException("User with specified email does not exist");
        }

        return user;
    }


    /**
     * Delete a user
     *
     * @param email - the user's email
     * @return the deleted user or null if there is no user with the given email
     */
    public User delete(String email) {
        User deletedUser = userRepository.delete(findUserByEmailId(email));

        if (deletedUser == null) {
            throw new UserNotFoundException("User with specified email doesn't exist!");
        }

        return deletedUser;
    }


    /**
     * Update a new user
     *
     * @param email     - the user's email
     * @param firstName - user's firstName
     * @param lastName  - user's lastName
     * @param city      - user's city
     * @return null- if the given user is updated
     * otherwise returns the user (id user exists)
     */
    public User update(String email, String firstName, String lastName, String city) {

        //to be continued

        try {
            User updatedUser = new User(email, firstName, lastName, city);
            User updated = userRepository.update(updatedUser);

            return updated;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ValidationException v) {
            System.out.println(v.getMessage());
        }
        return null;
    }

    /**
     * Determine if a user is in a friendship
     *
     * @param user       - a user
     * @param friendship - a friendship
     * @return true if the user is in friendship, false otherwise
     */
    public boolean isInFriendship(User user, Friendship friendship) {
        return user.getId() == friendship.getIdUser1() || user.getId() == friendship.getIdUser2();
    }

    /**
     * Get the user's friend from a certain friendship
     *
     * @param user       - the user
     * @param friendship - the friendship in which the user takes part
     * @return the user's friend or null if the user is not in the friendship
     */
    public User getFriend(User user, Friendship friendship) {
        if (isInFriendship(user, friendship)) {
            if (user.getId() != friendship.getIdUser1())
                return findOne(friendship.getIdUser1());
            return findOne(friendship.getIdUser2());
        }
        return null;
    }

}
