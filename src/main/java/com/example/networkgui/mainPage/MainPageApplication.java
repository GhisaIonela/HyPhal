package com.example.networkgui.mainPage;

import com.company.controller.Controller;
import com.company.repository.db.FriendRequestsDbRepository;
import com.company.repository.db.FriendshipDbRepository;
import com.company.repository.db.MessageDbRepository;
import com.company.repository.db.UserDbRepository;
import com.company.service.*;
import com.company.validators.FriendshipValidator;
import com.company.validators.UserValidator;
import com.example.networkgui.SceneController;
import com.example.networkgui.ServiceManager;
import com.example.networkgui.SuperController;
import com.example.networkgui.config.DatabaseConnectionCredentials;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
        MessageService messageService = new MessageService(messageDbRepository);

        FriendRequestsDbRepository friendRequestsDbRepository = new FriendRequestsDbRepository(dbConnectCred.getUrl(),
                dbConnectCred.getUsername(), dbConnectCred.getPassword());

        FriendRequestService friendRequestService = new FriendRequestService(userRepoDb, friendshipRepoDb, friendRequestsDbRepository);
        Controller controller = new Controller(userService2, friendshipService2, network, loginManager, messageService, friendRequestService);

        SuperController.setController(controller);
        SuperController.setLoginManager(loginManager);

        loginManager.login("mariaalmasan@gmail.com","12345");

        launch();
    }
}
