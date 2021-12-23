package com.example.networkgui.mainPage;

import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.dto.UserFriendshipDTO;
import com.example.networkgui.ServiceManager;
import com.example.networkgui.customWidgets.FriendCellFactory;
import com.example.networkgui.customWidgets.FriendCellFactory2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FriendsController {
    private User user = new User();
    private ServiceManager serviceManager;

    @FXML
    private ListView<UserFriendshipDTO> friendshipListView;

    @FXML
    private Label label;

    public void setUser(User user) {
        this.user = user;
    }

    public void setService(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public void loadContent() {
        ObservableList<UserFriendshipDTO> friendships = FXCollections.observableArrayList();
        friendships.setAll(serviceManager.getController().findUserFriendships(user.getEmail()));
        friendshipListView.setCellFactory(new FriendCellFactory());
        //friendshipListView.setCellFactory(new FriendCellFactory2());
        friendshipListView.setItems(friendships);


    }
}
