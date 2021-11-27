package com.company;

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

        Repository<Long, User> userRepoDb = new UserDbRepository("jdbc:postgresql://localhost:5432/laborator",
                "postgres", "postgres", new UserValidator());
        Repository<Long, Friendship> friendshipRepoDb = new FriendshipDbRepository("jdbc:postgresql://localhost:5432/laborator",
                "postgres", "postgres", new FriendshipValidator());

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