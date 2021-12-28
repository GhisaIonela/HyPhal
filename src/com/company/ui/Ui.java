package com.company.ui;

import com.company.controller.Controller;
import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.dto.ConversationDTO;
import com.company.exceptions.LoginException;
import com.company.exceptions.ServiceException;
import com.company.exceptions.UserNotFoundException;
import com.company.utils.Constants;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
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

    private AdminOption getAdminOption(String[] args){
        try{
            return AdminOption.valueOf(args[0]);

        } catch (IllegalArgumentException e) {
            System.out.println("Optiune invalida");
        }
        return null;
    }

    private UserOption getUserOption(String[] args){
        try{
            return UserOption.valueOf(args[0]);

        } catch (IllegalArgumentException e) {
            System.out.println("Optiune invalida");
        }
        return null;
    }

    private UserWelcomeOption getUserWelcomeOption(String[] args){
        try{
            return UserWelcomeOption.valueOf(args[0]);

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
                getMsg2Users [email user1] [email user2]
                """);
    }

    private void adminView() {
        boolean ok = true;
        Scanner s = new Scanner(System.in);
        String args = "";
        while (ok) {
            System.out.println("""
                -----  Admin view -----
                                
                To see available commands: help
                To go back: back
                """);
            args = s.nextLine();
            String[] tokens = args.split("\s");
            AdminOption option = getAdminOption(tokens);
            try{
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
                        controller.findUserFriendships(tokens[1])
                                .forEach(System.out::println);
                    } else {
                        throw new IllegalArgumentException("Invalid option for find user friendships");
                    }
                }
                case findUserFriendshipsByMonth -> {
                    if (tokens.length == 3) {
                        controller.findUserFriendshipsByMonth(tokens[1], LocalDateTime.parse(tokens[2], Constants.MONTH_FORMATTER).getMonth())
                                .forEach(System.out::println);
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
                case getMsg2Users -> {
                    if(tokens.length == 3){
                        controller.GetSortedMessagesBetweenTwoUsersByDate(tokens[1], tokens[2]).forEach(message -> System.out.println(message + "\n"));
                    }else{
                        throw new IllegalArgumentException("Invalid option for get messages between two users");
                    }
                }
                case help -> {
                    infoCommands();
                }
                case back -> {
                    ok = false;
                }
            } } catch (ServiceException se) {
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


    private void welcomeUserScreen(){
        boolean ok = true;
        Scanner s = new Scanner(System.in);
        String args = "";
        while (ok) {
            System.out.println("""
            ----- Welcome! -----
            
            To login type: login [email] [password]
            to create an account type: createAccount [email] [password] [first name] [last name] [city] [date of birth]
            
            To go back: back
            """);
            args = s.nextLine();
            String[] tokens = args.split("\s");
            UserWelcomeOption option = getUserWelcomeOption(tokens);
            try{
                switch (Objects.requireNonNull(option)) {
                    case login -> {
                        if (tokens.length == 3) {
                            try {
                                controller.login(tokens[1], tokens[2]);
                                userProfile(controller.findUserByEmail(tokens[1]));
                            } catch (LoginException loginException) {
                                System.out.println(loginException.getMessage());
                            }
                        } else {
                            throw new IllegalArgumentException("Invalid option for login");
                        }
                    }
                    case createAccount -> {
                        if (tokens.length == 7){
                            controller.createAccount(tokens[1], tokens[3], tokens[4], tokens[5], LocalDateTime.parse(tokens[6],Constants.DATE_OF_BIRTH_FORMATTER), tokens[2]);
                            userProfile(controller.findUserByEmail(tokens[1]));
                        } else {
                            throw new IllegalArgumentException("Invalid option for createAccount");
                        }
                    }
                    case back -> {
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

    private void userProfile(User user){
        boolean ok = true;
        Scanner s = new Scanner(System.in);
        String args = "";
        while (ok) {
            System.out.println(String.format("""
                -----   %s %s   -----
                
                showFriends
                showFriendsByMonth [month]
                
                chat
                messageMultipleUsers
                
                showFriendRequests
                sendFriendRequest [user email]
                cancelFriendRequest [user email]
                acceptFriendRequest [email]
                denyFriendRequest [email]
                
                logout
                """, user.getFirstName(), user.getLastName()));
            args = s.nextLine();
            String[] tokens = args.split("\s");
            UserOption userOption = getUserOption(tokens);
            try {
                switch (Objects.requireNonNull(userOption)) {
                    case showFriends-> {
                            controller.findUserFriendships(user.getEmail()).forEach(System.out::println);
                    }
                    case showFriendsByMonth -> {
                        if (tokens.length == 2) {
                            controller.findUserFriendshipsByMonth(user.getEmail(), LocalDateTime.parse(tokens[1], Constants.MONTH_FORMATTER).getMonth())
                                    .forEach(System.out::println);
                        } else {
                            throw new IllegalArgumentException("Invalid option for find user friendships by month");
                        }
                    }
                    case showFriendRequests -> {
                        System.out.println("Received:");
                        controller.findReceivedUserFriendRequests(user.getEmail()).forEach(friendRequestDTO -> {System.out.print(friendRequestDTO.senderToString());});
                        System.out.println("\nSent:");
                        controller.findSentUserFriendRequests(user.getEmail()).forEach(friendRequestDTO -> {System.out.print(friendRequestDTO.receiverToString());});
                        System.out.print("\n");
                    }
                    case sendFriendRequest -> {
                        if(tokens.length == 2) {
                            controller.sendFriendRequest(user.getEmail(), tokens[1]);
                        } else {
                            throw new IllegalArgumentException("Invalid option for send friend request");
                        }
                    }
                    case cancelFriendRequest -> {
                        if(tokens.length == 2) {
                            controller.cancelFriendRequest(user.getEmail(), tokens[1]);
                        } else {
                            throw new IllegalArgumentException("Invalid option for cancel friend request");
                        }
                    }
                    case acceptFriendRequest -> {
                        if(tokens.length == 2) {
                            controller.acceptFriendRequest(tokens[1], user.getEmail());
                        } else {
                            throw new IllegalArgumentException("Invalid option for accept friend request");
                        }
                    }
                    case denyFriendRequest -> {
                        if(tokens.length == 2) {
                            controller.denyFriendRequest(tokens[1], user.getEmail());
                        } else {
                            throw new IllegalArgumentException("Invalid option for deny friend request");
                        }
                    }
                    case chat -> {
                        startConversation();
                    }
                    case messageMultipleUsers -> {
                        messageMultipleUsers();
                    }

                    case logout -> {
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

    private void startConversation(){
        System.out.println("You can chat with: ");
        Iterable<ConversationDTO> availableConversations = controller.getConversationsInfo();
        availableConversations.forEach(System.out::println);
        System.out.println("Type the user's email with you want to talk or exit to go back");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();
        if(!option.equals("exit")){
            ConversationUi conversationUi= new ConversationUi(controller.createConversation(option));
            conversationUi.runConversation();
        }
    }

    private void messageMultipleUsers(){
        System.out.println("You can chat with: ");
        Iterable<ConversationDTO> availableConversations = controller.getConversationsInfo();
        availableConversations.forEach(System.out::println);
        System.out.println("Type the user's email separated by space");
        Scanner scanner = new Scanner(System.in);
        String emails = scanner.nextLine();
        List<String> splitedEmails;
        splitedEmails = List.of(emails.split("\s"));
        System.out.println("Write the message");
        Scanner scanner2 = new Scanner(System.in);
        String message = scanner2.nextLine();
        controller.sendMessageToMultipleUsers(splitedEmails, message, null);
    }

    public void run() {
        boolean ok = true;
        Scanner s = new Scanner(System.in);
        String args = "";
        while (ok) {
            System.out.println("""
                Which view would you like?
                
                adminView
                userView
                """);
            args = s.nextLine();
            String[] tokens = args.split("\s");
            Option option = getOption(tokens);
            try {
                switch (Objects.requireNonNull(option)) {
                    case adminView -> {
                        adminView();
                    }
                    case userView -> {
                        welcomeUserScreen();
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
