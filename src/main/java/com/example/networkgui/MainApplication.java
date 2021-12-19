package com.example.networkgui;

import com.company.config.DatabaseConnectionCredentials;
import com.company.controller.Controller;
import com.company.repository.db.FriendRequestsDbRepository;
import com.company.repository.db.FriendshipDbRepository;
import com.company.repository.db.MessageDbRepository;
import com.company.repository.db.UserDbRepository;
import com.company.service.*;
import com.company.validators.FriendshipValidator;
import com.company.validators.UserValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //DatabaseConnectionCredentials dbConnectCred = DatabaseConnectionCredentials.getInstance();
        //UserDbRepository userRepoDb = new UserDbRepository(dbConnectCred.getUrl(), dbConnectCred.getUsername(), dbConnectCred.getPassword(), new UserValidator());
        UserDbRepository userRepoDb = new UserDbRepository("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres", new UserValidator());
        //FriendshipDbRepository friendshipRepoDb = new FriendshipDbRepository(dbConnectCred.getUrl(), dbConnectCred.getUsername(), dbConnectCred.getPassword(), new FriendshipValidator());
        FriendshipDbRepository friendshipRepoDb = new FriendshipDbRepository("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres", new FriendshipValidator());
        UserService userService2 = new UserService(userRepoDb, friendshipRepoDb);
        FriendshipService friendshipService2 = new FriendshipService(friendshipRepoDb, userRepoDb);
        Network network = Network.getInstance();
        network.setFriendshipRepository(friendshipRepoDb);
        network.setUserRepository(userRepoDb);
        LoginManager loginManager = new LoginManager(userRepoDb);
        //MessageDbRepository messageDbRepository = new MessageDbRepository(dbConnectCred.getUrl(), dbConnectCred.getUsername(), dbConnectCred.getPassword());
        MessageDbRepository messageDbRepository = new MessageDbRepository("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres");
        MessageService messageService = new MessageService(messageDbRepository);
        FriendRequestsDbRepository friendRequestsDbRepository = new FriendRequestsDbRepository("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres");
        FriendRequestService friendRequestService = new FriendRequestService(userRepoDb, friendshipRepoDb, friendRequestsDbRepository);
        Controller controller = new Controller(userService2, friendshipService2, network, loginManager, messageService, friendRequestService);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();

        LoginController loginController = fxmlLoader.getController();
        loginController.setController(controller);

        SceneController.setStage(stage);

        Scene scene = new Scene(root, 930, 560);
        stage.setTitle("Social Network!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}