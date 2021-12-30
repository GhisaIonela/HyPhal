package com.example.networkgui.mainPage;

import com.example.networkgui.SceneController;
import com.example.networkgui.SuperController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainPageController extends SuperController{
    protected Node profilePage;
    protected Node friendsPage;
    protected Node messagesPage;
    protected Node inboxPage;


    public MainPageController() throws IOException {
        profilePage = new FXMLLoader(getClass().getResource("profile-view.fxml")).load();
        friendsPage = new FXMLLoader(getClass().getResource("friends-view.fxml")).load();
        messagesPage = new FXMLLoader(getClass().getResource("message-view.fxml")).load();
        inboxPage = new FXMLLoader(getClass().getResource("inbox-view.fxml")).load();
    }

    @FXML
    private BorderPane mainPage;

    @FXML
    private void handleProfileButtonAction(ActionEvent event) {
        mainPage.setCenter(profilePage);
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
    private void handleLogOutButtonAction(ActionEvent event) {
        try {
            SceneController.switchToAnotherScene("login-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        mainPage.setCenter(profilePage);

    }

    public void handleInboxButtonAction(ActionEvent actionEvent) {
        mainPage.setCenter(inboxPage);
    }



}
