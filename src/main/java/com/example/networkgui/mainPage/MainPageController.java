package com.example.networkgui.mainPage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainPageController {

    @FXML
    private BorderPane mainPage;
    @FXML
    private void handleProfileButtonAction(ActionEvent event) {
        try {
            mainPage.setCenter((Node) FXMLLoader.load(ProfileController.class.getResource("profile-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFeedButtonAction(ActionEvent event) {
        try {
            mainPage.setCenter((Node) FXMLLoader.load(ProfileController.class.getResource("feed-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFriendsButtonAction(ActionEvent event) {
        try {
            mainPage.setCenter((Node) FXMLLoader.load(ProfileController.class.getResource("friends-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMessagesButtonAction(ActionEvent event) {
        try {
            mainPage.setCenter((Node) FXMLLoader.load(ProfileController.class.getResource("messages-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSettingsButtonAction(ActionEvent event) {
        try {
            mainPage.setCenter((Node) FXMLLoader.load(ProfileController.class.getResource("settings-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
