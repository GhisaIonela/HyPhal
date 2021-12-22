package com.example.networkgui.mainPage;

import com.company.domain.User;
import com.company.dto.UserFriendshipDTO;
import com.example.networkgui.ServiceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class FriendsController {
    private User user = new User();
    private ServiceManager serviceManager;

    @FXML
    private Label label;

    public void setUser(User user) {
        this.user = user;
    }

    public void setService(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

}
