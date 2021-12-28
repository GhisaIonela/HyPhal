package com.company;

import com.company.config.DatabaseConnectionCredentials;
import com.company.controller.Controller;
import com.company.domain.*;
import com.company.exceptions.ValidationException;
import com.company.listen.Listener;
import com.company.repository.Repository;
import com.company.repository.db.FriendRequestsDbRepository;
import com.company.repository.db.FriendshipDbRepository;
import com.company.repository.db.MessageDbRepository;
import com.company.repository.db.UserDbRepository;
import com.company.service.*;
import com.company.ui.Ui;
import com.company.validators.FriendshipValidator;
import com.company.validators.UserValidator;
import com.company.validators.Validator;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


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

        FriendRequestsDbRepository friendRequestsDbRepository = new FriendRequestsDbRepository(dbConnectCred.getUrl(),
                dbConnectCred.getUsername(), dbConnectCred.getPassword());

        FriendRequestService friendRequestService = new FriendRequestService(userRepoDb, friendshipRepoDb, friendRequestsDbRepository);
        Controller controller = new Controller(userService2, friendshipService2, network, loginManager, messageService, friendRequestService);
        try {
            Connection connection = DriverManager.getConnection(dbConnectCred.getUrl(), dbConnectCred.getUsername(), dbConnectCred.getPassword());
            Listener listener = new Listener(connection, "message_receiver");
            Listener listener2 = new Listener(connection, "message");
            listener.start();
            listener2.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Ui ui = new Ui(controller);
        //ui.run();


    }
}

