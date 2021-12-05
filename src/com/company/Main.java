package com.company;

import com.company.config.DatabaseConnectionCredentials;
import com.company.controller.Controller;
import com.company.domain.Friendship;
import com.company.domain.Message;
import com.company.domain.User;
import com.company.repository.Repository;
import com.company.repository.db.FriendshipDbRepository;
import com.company.repository.db.MessageDbRepository;
import com.company.repository.db.UserDbRepository;
import com.company.service.FriendshipService;
import com.company.service.LoginManager;
import com.company.service.Network;
import com.company.service.UserService;
import com.company.ui.Ui;
import com.company.validators.FriendshipValidator;
import com.company.validators.UserValidator;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        DatabaseConnectionCredentials dbConnectCred =  DatabaseConnectionCredentials.getInstance();


        UserDbRepository userRepoDb = new UserDbRepository(dbConnectCred.getUrl(),
                dbConnectCred.getUsername(), dbConnectCred.getPassword(), new UserValidator());
        FriendshipDbRepository friendshipRepoDb = new FriendshipDbRepository(dbConnectCred.getUrl(),
                dbConnectCred.getUsername(), dbConnectCred.getPassword(), new FriendshipValidator());

//        UserService userService2 = new UserService(userRepoDb, friendshipRepoDb);
//        FriendshipService friendshipService2 = new FriendshipService(friendshipRepoDb, userRepoDb);
//        Network network = Network.getInstance();
//        network.setFriendshipRepository(friendshipRepoDb);
//        network.setUserRepository(userRepoDb);
//
//        LoginManager loginManager= new LoginManager(userRepoDb);
//        Controller controller = new Controller(userService2, friendshipService2, network, loginManager);
//
//        Ui ui = new Ui(controller);
//        ui.run();

        MessageDbRepository messageDbRepo = new MessageDbRepository(dbConnectCred.getUrl(),
                dbConnectCred.getUsername(), dbConnectCred.getPassword());


//        List<User> receivers1 = new ArrayList<>();
//        receivers1.add(userRepoDb.findOne(7L));
//        Message message = new Message(userRepoDb.findOne(6L), receivers1, "first message");
//        messageDbRepo.save(message);
//
//
//        List<User> receivers2 = new ArrayList<>();
//        receivers2.add(userRepoDb.findOne(6L));
//        Message message2 = new Message(userRepoDb.findOne(7L), receivers2, "second message(replay 1)");
//        message.setId(20L);
//        message2.setReplay(message);
//        messageDbRepo.save(message2);
//
//
//
//        List<User> receivers3 = new ArrayList<>();
//        receivers3.add(userRepoDb.findOne(7L));
//        Message message3 = new Message(userRepoDb.findOne(6L), receivers3, "third message(replay 2)");
//        message2.setId(21L);
//        message3.setReplay(message2);
//        messageDbRepo.save(message3);

        //System.out.println(messageDbRepo.findOne(22L));
        //messageDbRepo.findAll().forEach(System.out::println);
        //messageDbRepo.getSortedMesssagesByDateTwoUsers(6L, 7L).forEach(System.out::println);
    }
}

