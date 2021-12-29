package com.example.networkgui.mainPage;

import com.company.controller.Controller;
import com.company.repository.db.FriendRequestsDbRepository;
import com.company.repository.db.FriendshipDbRepository;
import com.company.repository.db.MessageDbRepository;
import com.company.repository.db.UserDbRepository;
import com.company.service.*;
import com.company.validators.FriendshipValidator;
import com.company.validators.UserValidator;
import com.example.networkgui.SuperController;
import com.example.networkgui.config.DatabaseConnectionCredentials;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainPageApplication extends Application {


    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("mainPage-view.fxml"));
        AnchorPane root = loader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("HyPhal");
        stage.show();
    }

    public static void main(String[] args) {

       try{
           //DatabaseConnectionCredentials dbConnectCred = DatabaseConnectionCredentials.getInstance();
           Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres");

           UserDbRepository userRepoDb = new UserDbRepository(connection, new UserValidator());
           FriendshipDbRepository friendshipRepoDb = new FriendshipDbRepository(connection, new FriendshipValidator());

           UserService userService2 = new UserService(userRepoDb, friendshipRepoDb);
           FriendshipService friendshipService2 = new FriendshipService(friendshipRepoDb, userRepoDb);

           Network network = Network.getInstance();
           network.setUserRepository(userRepoDb);
           network.setFriendshipRepository(friendshipRepoDb);

           LoginManager loginManager = new LoginManager(userRepoDb);
           MessageDbRepository messageDbRepository = new MessageDbRepository(connection);
           MessageService messageService = new MessageService(messageDbRepository, userRepoDb);

           FriendRequestsDbRepository friendRequestsDbRepository = new FriendRequestsDbRepository(connection);

           FriendRequestService friendRequestService = new FriendRequestService(userRepoDb, friendshipRepoDb, friendRequestsDbRepository);
           Controller controller = new Controller(userService2, friendshipService2, network, loginManager, messageService, friendRequestService);

           SuperController.setController(controller);
           SuperController.setLoginManager(loginManager);

           launch();
           connection.close();
           System.out.println("dasda");
       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }

    }
}
