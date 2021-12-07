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
import com.company.service.*;
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

        UserService userService2 = new UserService(userRepoDb, friendshipRepoDb);
        FriendshipService friendshipService2 = new FriendshipService(friendshipRepoDb, userRepoDb);
        Network network = Network.getInstance();
        network.setFriendshipRepository(friendshipRepoDb);
        network.setUserRepository(userRepoDb);

        LoginManager loginManager= new LoginManager(userRepoDb);
        MessageDbRepository messageDbRepository = new MessageDbRepository(dbConnectCred.getUrl(),
                dbConnectCred.getUsername(), dbConnectCred.getPassword());
        MessageService messageService = new MessageService(messageDbRepository);
        Controller controller = new Controller(userService2, friendshipService2, network, loginManager, messageService);

        Ui ui = new Ui(controller);
        ui.run();

//        MessageDbRepository messageDbRepo = new MessageDbRepository(dbConnectCred.getUrl(),
//                dbConnectCred.getUsername(), dbConnectCred.getPassword());



    }
}

