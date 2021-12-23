package com.example.networkgui.mainPage;

import com.company.domain.User;
import com.example.networkgui.ServiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainPageController {
    private User user = new User();
    private ServiceManager serviceManager;

    @FXML
    private BorderPane mainPage;

    @FXML
    private Node defaultPage;

    @FXML
    private void handleProfileButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
            Parent root = loader.load();
            ProfileController profileController = loader.getController();
            profileController.setUser(user);
            profileController.setService(serviceManager);
            profileController.loadContent();

            mainPage.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFeedButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("feed-view.fxml"));
            Parent root = loader.load();
            FeedController feedController = loader.getController();
            feedController.setUser(user);
            feedController.setService(serviceManager);

            mainPage.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFriendsButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("friends-view.fxml"));
            Parent root = loader.load();
            FriendsController friendsController = loader.getController();
            friendsController.setUser(user);
            friendsController.setService(serviceManager);
            friendsController.loadContent();

            mainPage.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMessagesButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("messages-view.fxml"));
            Parent root = loader.load();
            MessagesController messagesController = loader.getController();
            messagesController.setUser(user);
            messagesController.setService(serviceManager);

            mainPage.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSettingsButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("settings-view.fxml"));
            Parent root = loader.load();
            SettingsController settingsController = loader.getController();
            settingsController.setUser(user);
            settingsController.setService(serviceManager);

            mainPage.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setService(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public void setDefaultPage(Node node) {
        mainPage.setCenter(node);
    }
}
