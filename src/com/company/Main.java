package com.company;

import com.company.config.DatabaseConnectionCredentials;
import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.repository.Repository;
import com.company.repository.db.FriendshipDbRepository;
import com.company.repository.db.UserDbRepository;
import com.company.service.FriendshipService;
import com.company.service.Network;
import com.company.service.UserService;
import com.company.ui.Ui;
import com.company.validators.FriendshipValidator;
import com.company.validators.UserValidator;


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
        Ui ui = new Ui(userService2, friendshipService2, network);
        ui.run();

    }
}

//this is the branch for Lab4