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
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        stage.setTitle("HyPhal!");
        SceneController.setStage(stage);
        SuperController.setStage(stage);
        SceneController.switchToAnotherScene("login-view.fxml");

    }

    public static void main(String[] args) {
        try {
            DatabaseConnectionCredentials dbConnectCred = DatabaseConnectionCredentials.getInstance();
            Connection connection = DriverManager.getConnection(dbConnectCred.getUrl(), dbConnectCred.getUsername(), dbConnectCred.getPassword());

            UserDbRepository userRepoDb = new UserDbRepository(connection, new UserValidator());
            FriendshipDbRepository friendshipRepoDb = new FriendshipDbRepository(connection, new FriendshipValidator());
            UserService userService2 = new UserService(userRepoDb, friendshipRepoDb);
            FriendshipService friendshipService2 = new FriendshipService(friendshipRepoDb, userRepoDb);
            Network network = Network.getInstance();
            network.setFriendshipRepository(friendshipRepoDb);
            network.setUserRepository(userRepoDb);
            LoginManager loginManager = new LoginManager(userRepoDb);
            MessageDbRepository messageDbRepository = new MessageDbRepository(connection);
            MessageService messageService = new MessageService(messageDbRepository, userRepoDb);
            FriendRequestsDbRepository friendRequestsDbRepository = new FriendRequestsDbRepository(connection);
            FriendRequestService friendRequestService = new FriendRequestService(userRepoDb, friendshipRepoDb, friendRequestsDbRepository);
            Controller controller = new Controller(userService2, friendshipService2, network, loginManager, messageService, friendRequestService);


            SuperController.setConnection(connection);
            SuperController.setController(controller);
            SuperController.setLoginManager(loginManager);
            launch();
            //connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}