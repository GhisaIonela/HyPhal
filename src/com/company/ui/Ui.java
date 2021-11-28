package com.company.ui;

import com.company.domain.Friendship;
import com.company.service.FriendshipService;
import com.company.service.Network;
import com.company.service.UserService;

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
        System.out.println("saveUser [id] [first name] [last name]\n" +
                            "findUser [id]\n" +
                            "findAllUsers\n" +
                            "deleteUser [id]\n" +
                "updateUser [id] [new first name] [new last name]\n" +
                "saveFriendship [id] [id user1] [id user2]\n" +
                "findFriendship [id]\n" +
                "findAllFreindships\n" +
                "deleteFriendship [id]\n" +
                "updateFriendship [id] [new id user1] [new id user2]\n"+
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
                        if (tokens.length == 4) {
                            userService.save(Long.valueOf(tokens[1]), tokens[2], tokens[3]);
                        } else {
                            throw new IllegalArgumentException("Invalid option for save user");
                        }
                    }
                    case findUser -> {
                        if (tokens.length == 2) {
                            System.out.println(userService.findOne(Long.valueOf(tokens[1])));
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
                            userService.delete(Long.valueOf(tokens[1]));
                        } else {
                            throw new IllegalArgumentException("Invalid option for delete user");
                        }
                    }
                    case updateUser -> {
                        if (tokens.length == 4) {
                            userService.update(Long.valueOf(tokens[1]), tokens[2], tokens[3]);
                        } else {
                            throw new IllegalArgumentException("Invalid option for update user");
                        }
                    }
                    case saveFriendship -> {
                        if (tokens.length == 4) {
                             friendshipService.save(Long.valueOf(tokens[1]), Long.valueOf(tokens[2]), Long.valueOf(tokens[3]));
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
                            if(friendship!=null){
                                System.out.println("Friendship has been deleted");
                            }
                        } else {
                            throw new IllegalArgumentException("Invalid option for delete friendship");
                        }
                    }
                    case updateFriendship -> {
                        if (tokens.length == 5) {
                            friendshipService.update(Long.valueOf(tokens[1]), Long.valueOf(tokens[2]), Long.valueOf(tokens[3]),
                                    LocalDateTime.parse(tokens[4], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        } else {
                            throw new IllegalArgumentException("Invalid option for update friendship");
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
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            catch (NullPointerException e){
                System.out.println("The option doesn't match");
            }
        }
    }
}
