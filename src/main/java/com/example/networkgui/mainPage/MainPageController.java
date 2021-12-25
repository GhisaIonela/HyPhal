package com.example.networkgui.mainPage;

import com.company.domain.User;
import com.example.networkgui.SuperController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainPageController extends SuperController {
    protected User user;
    protected Node profilePage;
    protected Node feedPage;
    protected Node friendsPage;
    protected Node messagesPage;
    protected Node settingsPage;

    public MainPageController() throws IOException {
        user = loginManager.getLogged();
        profilePage = new FXMLLoader(getClass().getResource("profile-view.fxml")).load();
        feedPage = new FXMLLoader(getClass().getResource("feed-view.fxml")).load();
        friendsPage = new FXMLLoader(getClass().getResource("friends-view.fxml")).load();
        messagesPage = new FXMLLoader(getClass().getResource("messages-view.fxml")).load();
        settingsPage = new FXMLLoader(getClass().getResource("settings-view.fxml")).load();
    }

    @FXML
    private BorderPane mainPage;

    @FXML
    private void handleProfileButtonAction(ActionEvent event) {
        mainPage.setCenter(profilePage);
    }

    @FXML
    private void handleFeedButtonAction(ActionEvent event) {
        mainPage.setCenter(feedPage);
    }

    @FXML
    private void handleFriendsButtonAction(ActionEvent event) {
        mainPage.setCenter(friendsPage);
    }

    @FXML
    private void handleMessagesButtonAction(ActionEvent event) {
        mainPage.setCenter(messagesPage);
    }

    @FXML
    private void handleSettingsButtonAction(ActionEvent event) {
        mainPage.setCenter(settingsPage);
    }

    @FXML
    public void initialize() {
        mainPage.setCenter(feedPage);
    }
}
