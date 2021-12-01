package com.company.ui;

import com.company.domain.Friendship;
import com.company.exceptions.ServiceException;
import com.company.exceptions.UserNotFoundException;
import com.company.service.FriendshipService;
import com.company.service.Network;
import com.company.service.UserService;
import com.company.utils.Constants;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;


public class Ui {
    private UserService userService;
    private FriendshipService friendshipService;
    private Network network;

    public Ui(UserService userService, FriendshipService friendshipService, Network network) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.network = network;
        network.loadNetwork();
    }

    private Option getOption(String[] args){
        try{
            return Option.valueOf(args[0]);

        } catch (IllegalArgumentException e) {
            System.out.println("Optiune invalida");
        }
        return null;
    }

    private void infoCommands(){
        System.out.println("Available commands:");
        System.out.println("saveUser [email] [first name] [last name] [city] [date of birth]\n" +
                            "findUser [email]\n" +
                            "findAllUsers\n" +
                            "deleteUser [email]\n" +
                "updateUser [old email] [new email] [new first name] [new last name] [city] [date of birth]\n" +
                "saveFriendship [id user1] [id user2]\n" +
                "findFriendship [id]\n" +
                "findAllFriendships\n" +
                "deleteFriendship [id]\n"+
                "getCommunities\n"+
                "getMostSociable\n");
    }

    public void run(){
        infoCommands();
        boolean ok = true;
        Scanner s = new Scanner(System.in);
        String args = "";
        while(ok) {
            args = s.nextLine();
            String[] tokens = args.split("\s");
            Option option = getOption(tokens);
            network.loadNetwork();
            try {
                switch (Objects.requireNonNull(option)) {
                    case saveUser -> {
                        if (tokens.length == 6) {
                            userService.save(tokens[1], tokens[2], tokens[3], tokens[4], LocalDateTime.parse(tokens[5], Constants.DATE_OF_BIRTH_FORMATTER));
                        } else {
                            throw new IllegalArgumentException("Invalid option for save user");
                        }
                    }
                    case findUser -> {
                        if (tokens.length == 2) {
                            System.out.println(userService.findUserByEmail(tokens[1]));
                        } else {
                            throw new IllegalArgumentException("Invalid option for find user");
                        }
                    }
                    case findAllUsers -> {
                        if (tokens.length == 1) {
                            userService.findAll().forEach(System.out::println);
                        } else {
                            throw new IllegalArgumentException("Invalid option for find all users");
                        }
                    }
                    case deleteUser -> {
                        if (tokens.length == 2) {
                            userService.delete(tokens[1]);
                        } else {
                            throw new IllegalArgumentException("Invalid option for delete user");
                        }
                    }
                    case updateUser -> {
                        if (tokens.length == 7) {
                            userService.update(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], LocalDateTime.parse(tokens[6], Constants.DATE_OF_BIRTH_FORMATTER));
                        } else {
                            throw new IllegalArgumentException("Invalid option for update user");
                        }
                    }
                    case saveFriendship -> {
                        if (tokens.length == 3) {
                            friendshipService.save(Long.valueOf(tokens[1]), Long.valueOf(tokens[2]));
                        } else {
                            throw new IllegalArgumentException("Invalid option for save friendship");
                        }
                    }
                    case findFriendship -> {
                        if (tokens.length == 2) {
                            System.out.println(friendshipService.findOne(Long.valueOf(tokens[1])));
                        } else {
                            throw new IllegalArgumentException("Invalid option for find friendship");
                        }
                    }
                    case findAllFriendships -> {
                        if (tokens.length == 1) {
                            friendshipService.findAll().forEach(System.out::println);
                        } else {
                            throw new IllegalArgumentException("Invalid option for find all friendships");
                        }
                    }
                    case deleteFriendship -> {
                        if (tokens.length == 2) {
                            Friendship friendship = friendshipService.delete(Long.valueOf(tokens[1]));
                            if (friendship != null) {
                                System.out.println("Friendship has been deleted");
                            }
                        } else {
                            throw new IllegalArgumentException("Invalid option for delete friendship");
                        }
                    }
                    case getCommunities -> {
                        System.out.println(network.getNumberOfConnectedComponents());
                    }
                    case getMostSociable -> {
                        System.out.println("The most sociable community is:");
                        System.out.println(network.getMostSociableCommunity());
                    }
                    case exit -> {
                        ok = false;
                    }
                }
            } catch (ServiceException se) {
                System.out.println(se.getMessage());
            } catch (UserNotFoundException unfe){
                System.out.println(unfe.getMessage());
            } catch (DateTimeException de){
                System.out.println(de.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            catch (NullPointerException e){
                System.out.println("The option doesn't match");
            }
        }
    }
}
