package com.example.networkgui.mainPage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainPageController {

    @FXML
    private Pane navigationTab;

    @FXML
    private Pane contentPage;

    @FXML
    private void handleProfileButtonAction(ActionEvent event) {
        try {
            contentPage.getChildren().setAll((Node) FXMLLoader.load(ProfileController.class.getResource("profile-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFeedButtonAction(ActionEvent event) {
        try {
            contentPage.getChildren().setAll((Node) FXMLLoader.load(FeedController.class.getResource("feed-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFriendsButtonAction(ActionEvent event) {
        try {
            contentPage.getChildren().setAll((Node) FXMLLoader.load(FeedController.class.getResource("friends-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMessagesButtonAction(ActionEvent event) {
        try {
            contentPage.getChildren().setAll((Node) FXMLLoader.load(FeedController.class.getResource("messages-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSettingsButtonAction(ActionEvent event) {
        try {
            contentPage.getChildren().setAll((Node) FXMLLoader.load(FeedController.class.getResource("settings-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
