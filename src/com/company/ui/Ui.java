package com.company.ui;

import com.company.controller.Controller;
import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.exceptions.ServiceException;
import com.company.exceptions.UserNotFoundException;
import com.company.service.FriendshipService;
import com.company.service.Network;
import com.company.service.UserService;
import com.company.utils.Constants;

import java.text.DateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;


public class Ui {
    private Controller controller;

    public Ui(Controller controller) {
        this.controller = controller;
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
        System.out.println("""
                saveUser [email] [first name] [last name] [city] [date of birth] [password]
                findUser [email]
                findAllUsers
                deleteUser [email]
                updateUser [old email] [new email] [new first name] [new last name] [city] [date of birth] [password]
                saveFriendship [id user1] [id user2]
                findFriendship [id]
                findAllFriendships
                deleteFriendship [id]
                getCommunities
                getMostSociable
                findUserFriendships [email]
                findUserFriendshipsByMonth [email] [month]
                """);
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
            try {
                switch (Objects.requireNonNull(option)) {
                    case saveUser -> {

                        if (tokens.length == 7) {
                            controller.saveUser(tokens[1], tokens[2], tokens[3], tokens[4], LocalDateTime.parse(tokens[5], Constants.DATE_OF_BIRTH_FORMATTER), tokens[6]);

                        } else {
                            throw new IllegalArgumentException("Invalid option for save user");
                        }
                    }
                    case findUser -> {
                        if (tokens.length == 2) {
                            System.out.println(controller.findUserByEmail(tokens[1]));
                        } else {
                            throw new IllegalArgumentException("Invalid option for find user");
                        }
                    }
                    case findAllUsers -> {
                        if (tokens.length == 1) {
                            controller.findAllUsers().forEach(System.out::println);
                        } else {
                            throw new IllegalArgumentException("Invalid option for find all users");
                        }
                    }
                    case deleteUser -> {
                        if (tokens.length == 2) {
                            controller.deleteUser(tokens[1]);
                        } else {
                            throw new IllegalArgumentException("Invalid option for delete user");
                        }
                    }
                    case updateUser -> {

                        if (tokens.length == 8) {
                            controller.updateUser(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], LocalDateTime.parse(tokens[6], Constants.DATE_OF_BIRTH_FORMATTER), tokens[7]);

                        } else {
                            throw new IllegalArgumentException("Invalid option for update user");
                        }
                    }
                    case findUserFriendships -> {
                        if (tokens.length == 2) {
                            User user = controller.findUserByEmail(tokens[1]);
                            controller.findUserFriendships(tokens[1]).forEach(
                                    friendship -> {
                                        if(friendship.getIdUser1().equals(user.getId()))
                                            System.out.printf("%s | %s | %s%n",
                                                    controller.findUserById(friendship.getIdUser2()).getLastName(),
                                                    controller.findUserById(friendship.getIdUser2()).getFirstName(),
                                                    friendship.getDateTime().format(Constants.DATE_TIME_FORMATTER));
                                        else System.out.printf("%s | %s | %s%n",
                                                controller.findUserById(friendship.getIdUser1()).getLastName(),
                                                controller.findUserById(friendship.getIdUser1()).getFirstName(),
                                                friendship.getDateTime().format(Constants.DATE_TIME_FORMATTER));
                                    }
                            );
                        } else {
                            throw new IllegalArgumentException("Invalid option for find user friendships");
                        }
                    }
                    case findUserFriendshipsByMonth -> {
                        if (tokens.length == 3) {
                            User user = controller.findUserByEmail(tokens[1]);
                            controller.findUserFriendshipsByMonth(tokens[1], LocalDateTime.parse(tokens[2], Constants.MONTH_FORMATTER).getMonth()).forEach(
                                    friendship -> {
                                        if(friendship.getIdUser1().equals(user.getId()))
                                            System.out.printf("%s | %s | %s%n",
                                                    controller.findUserById(friendship.getIdUser2()).getLastName(),
                                                    controller.findUserById(friendship.getIdUser2()).getFirstName(),
                                                    friendship.getDateTime().format(Constants.DATE_TIME_FORMATTER));
                                        else System.out.printf("%s | %s | %s%n",
                                                controller.findUserById(friendship.getIdUser1()).getLastName(),
                                                controller.findUserById(friendship.getIdUser1()).getFirstName(),
                                                friendship.getDateTime().format(Constants.DATE_TIME_FORMATTER));
                                    }
                            );
                        } else {
                            throw new IllegalArgumentException("Invalid option for find user friendships by month");
                        }
                    }
                    case saveFriendship -> {
                        if (tokens.length == 3) {
                            controller.saveFriendship(Long.valueOf(tokens[1]), Long.valueOf(tokens[2]));
                        } else {
                            throw new IllegalArgumentException("Invalid option for save friendship");
                        }
                    }
                    case findFriendship -> {
                        if (tokens.length == 2) {
                            System.out.println(controller.findFriendship(Long.valueOf(tokens[1])));
                        } else {
                            throw new IllegalArgumentException("Invalid option for find friendship");
                        }
                    }
                    case findAllFriendships -> {
                        if (tokens.length == 1) {
                            controller.findAllFriendships().forEach(System.out::println);
                        } else {
                            throw new IllegalArgumentException("Invalid option for find all friendships");
                        }
                    }
                    case deleteFriendship -> {
                        if (tokens.length == 2) {
                            Friendship friendship = controller.deleteFriendship(Long.valueOf(tokens[1]));
                            if (friendship != null) {
                                System.out.println("Friendship has been deleted");
                            }
                        } else {
                            throw new IllegalArgumentException("Invalid option for delete friendship");
                        }
                    }
                    case getCommunities -> {
                        System.out.println(controller.getNumberOfConnectedComponents());
                    }
                    case getMostSociable -> {
                        System.out.println("The most sociable community is:");
                        System.out.println(controller.getMostSociableCommunity());
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
