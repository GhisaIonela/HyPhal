package com.example.networkgui.mainPage;

import com.company.controller.Controller;
import com.company.domain.User;
import com.company.service.LoginManager;
import com.example.networkgui.SceneController;
import com.example.networkgui.ServiceManager;
import com.example.networkgui.SuperController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainPageController extends SuperController {
    protected User user;

    public MainPageController() {
        user = loginManager.getLogged();
    }

    @FXML
    private BorderPane mainPage;

    @FXML
    private Node defaultPage;

    @FXML
    private void handleProfileButtonAction(ActionEvent event) {
        try {

            mainPage.setCenter(new FXMLLoader(getClass().getResource("profile-view.fxml")).load());

            //mainPage.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFeedButtonAction(ActionEvent event) {
        try {

            mainPage.setCenter(new FXMLLoader(getClass().getResource("feed-view.fxml")).load());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFriendsButtonAction(ActionEvent event) {
        try {

            mainPage.setCenter(new FXMLLoader(getClass().getResource("friends-view.fxml")).load());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMessagesButtonAction(ActionEvent event) {
        try {

            mainPage.setCenter(new FXMLLoader(getClass().getResource("messages-view.fxml")).load());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSettingsButtonAction(ActionEvent event) {
        try {

            mainPage.setCenter(new FXMLLoader(getClass().getResource("settings-view.fxml")).load());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        try {
            mainPage.setCenter(FXMLLoader.load(FeedController.class.getResource("feed-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
