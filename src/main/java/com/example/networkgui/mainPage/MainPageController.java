package com.example.networkgui.mainPage;

import com.company.controller.Controller;
import com.company.domain.User;
import com.example.networkgui.ServiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainPageController {
    private User user;
    private ServiceManager serviceManager;

    @FXML
    private BorderPane mainPage;

    @FXML
    private Node defaultPage;

    @FXML
    private void handleProfileButtonAction(ActionEvent event) {
        try {

            //mainPage.setCenter(FXMLLoader.load(ProfileController.class.getResource("profile-view.fxml")));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("profile-view.fxml"));

            ProfileController profileController = new ProfileController();
            loader.setController(profileController);
            profileController.setUser(user);
            profileController.setService(serviceManager);

            mainPage.setCenter(loader.load());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFeedButtonAction(ActionEvent event) {
        try {
            mainPage.setCenter(FXMLLoader.load(FeedController.class.getResource("feed-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFriendsButtonAction(ActionEvent event) {
        try {
            mainPage.setCenter(FXMLLoader.load(FriendsController.class.getResource("friends-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMessagesButtonAction(ActionEvent event) {
        try {
            mainPage.setCenter(FXMLLoader.load(MessagesController.class.getResource("messages-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSettingsButtonAction(ActionEvent event) {
        try {
            mainPage.setCenter(FXMLLoader.load(SettingsController.class.getResource("settings-view.fxml")));
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
