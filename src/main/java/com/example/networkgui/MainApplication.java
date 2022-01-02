package com.example.networkgui;

import com.company.listen.Listener;
import com.example.networkgui.config.DatabaseConnectionCredentials;
import com.company.controller.Controller;
import com.company.repository.db.FriendRequestsDbRepository;
import com.company.repository.db.FriendshipDbRepository;
import com.company.repository.db.MessageDbRepository;
import com.company.repository.db.UserDbRepository;
import com.company.service.*;
import com.company.validators.FriendshipValidator;
import com.company.validators.UserValidator;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class MainApplication extends Application {
    private static Connection connection;


    @Override
    public void start(Stage stage) throws IOException {

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("HyPhal!");
        SceneController.setStage(stage);
        SuperController.setStage(stage);
        SceneController.switchToAnotherScene("login-view.fxml");

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        connection.close();
    }

    public static void main(String[] args) {

        try {
            DatabaseConnectionCredentials dbConnectCred = DatabaseConnectionCredentials.getInstance();
            connection = DriverManager.getConnection(dbConnectCred.getUrl(), dbConnectCred.getUsername(), dbConnectCred.getPassword());

            UserDbRepository userRepoDb = new UserDbRepository(connection, new UserValidator());
            FriendshipDbRepository friendshipRepoDb = new FriendshipDbRepository(connection, new FriendshipValidator());
            FriendRequestsDbRepository friendRequestsDbRepository = new FriendRequestsDbRepository(connection);
            UserService userService2 = new UserService(userRepoDb, friendshipRepoDb);
            FriendshipService friendshipService2 = new FriendshipService(friendshipRepoDb, userRepoDb, friendRequestsDbRepository);
            FriendRequestService friendRequestService = new FriendRequestService(userRepoDb, friendshipRepoDb, friendRequestsDbRepository);

            Network network = Network.getInstance();
            network.setFriendshipRepository(friendshipRepoDb);
            network.setUserRepository(userRepoDb);
            LoginManager loginManager = new LoginManager(userRepoDb);
            MessageDbRepository messageDbRepository = new MessageDbRepository(connection);
            MessageService messageService = new MessageService(messageDbRepository, userRepoDb);

            Controller controller = new Controller(userService2, friendshipService2, network, loginManager, messageService, friendRequestService);

            Listener listener = new Listener(connection, "changes");

            SuperController.setConnection(connection);
            SuperController.setController(controller);
            SuperController.setLoginManager(loginManager);
            SuperController.setDbListener(listener);
            listener.start();
            launch();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}