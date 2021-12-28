package com.example.networkgui;


import com.company.controller.Controller;
import com.company.repository.db.FriendRequestsDbRepository;
import com.company.repository.db.FriendshipDbRepository;
import com.company.repository.db.MessageDbRepository;
import com.company.repository.db.UserDbRepository;
import com.company.service.*;
import com.company.validators.FriendshipValidator;
import com.company.validators.UserValidator;
import com.example.networkgui.config.DatabaseConnectionCredentials;

public class ServiceManager {
    private Controller controller;

    public ServiceManager() {

        DatabaseConnectionCredentials dbConnectCred = DatabaseConnectionCredentials.getInstance();

        UserDbRepository userRepoDb = new UserDbRepository(dbConnectCred.getUrl(),
                dbConnectCred.getUsername(), dbConnectCred.getPassword(), new UserValidator());
        FriendshipDbRepository friendshipRepoDb = new FriendshipDbRepository(dbConnectCred.getUrl(),
                dbConnectCred.getUsername(), dbConnectCred.getPassword(), new FriendshipValidator());

        UserService userService2 = new UserService(userRepoDb, friendshipRepoDb);
        FriendshipService friendshipService2 = new FriendshipService(friendshipRepoDb, userRepoDb);

        Network network = Network.getInstance();
        network.setUserRepository(userRepoDb);
        network.setFriendshipRepository(friendshipRepoDb);

        LoginManager loginManager = new LoginManager(userRepoDb);
        MessageDbRepository messageDbRepository = new MessageDbRepository(dbConnectCred.getUrl(),
                dbConnectCred.getUsername(), dbConnectCred.getPassword());
        MessageService messageService = new MessageService(messageDbRepository, userRepoDb);

        FriendRequestsDbRepository friendRequestsDbRepository = new FriendRequestsDbRepository(dbConnectCred.getUrl(),
                dbConnectCred.getUsername(), dbConnectCred.getPassword());

        FriendRequestService friendRequestService = new FriendRequestService(userRepoDb, friendshipRepoDb, friendRequestsDbRepository);
        this.controller = new Controller(userService2, friendshipService2, network, loginManager, messageService, friendRequestService);
    }

    public Controller getController() {
        return this.controller;
    }

}
