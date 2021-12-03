package com.company.controller;

import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.service.FriendshipService;
import com.company.service.Network;
import com.company.service.UserService;
import com.company.utils.Constants;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Controller {
    private UserService userService;
    private FriendshipService friendshipService;
    private Network network;

    public Controller(UserService userService, FriendshipService friendshipService, Network network) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.network = network;
        network.loadNetwork();
    }

    public User saveUser(String email, String firstName, String lastName, String city, LocalDateTime dateOfBirth){
        return userService.save(email, firstName, lastName, city, dateOfBirth);
    }

    public User findUser(String email){
        return userService.findUserByEmail(email);
    }

    public Iterable<User> findAllUsers(){
        return userService.findAll();
    }

    public User deleteUser(String email){
        return userService.delete(email);
    }

    public User updateUser(String oldEmail, String newEmail, String firstName, String lastName, String city, LocalDateTime dateOfBirth){
        return userService.update(oldEmail, newEmail, firstName, lastName, city, dateOfBirth);
    }

    public Friendship saveFriendship(Long idUser1, Long idUser2){
        return friendshipService.save(idUser1, idUser2);
    }

    public Friendship findFriendship(Long id){
        return friendshipService.findOne(id);
    }

    public Iterable<Friendship> findAllFriendships(){
        return friendshipService.findAll();
    }

    public Friendship deleteFriendship(Long id){
        return friendshipService.delete(id);
    }

    public int getNumberOfConnectedComponents(){
        return network.getNumberOfConnectedComponents();
    }

    public List<Long> getMostSociableCommunity(){
        return network.getMostSociableCommunity();
    }

    public Iterable<Friendship> findUserFriendships(String email){
        User user = userService.findUserByEmail(email);
        return StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.getIdUser1().equals(user.getId()) || friendship.getIdUser2().equals(user.getId()))
                .collect(Collectors.toList());
    }

    public Iterable<Friendship> findtUserFriendshipsByMonth(String email, Month month) {
        User user = userService.findUserByEmail(email);
        return StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> (friendship.getIdUser1().equals(user.getId()) || friendship.getIdUser2().equals(user.getId()))
                                        && friendship.getDateTime().getMonth() == month)
                .collect(Collectors.toList());
    }
}
