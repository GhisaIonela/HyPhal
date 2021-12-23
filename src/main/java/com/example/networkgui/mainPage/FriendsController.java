package com.example.networkgui.mainPage;

import com.company.domain.User;
import com.company.dto.UserFriendshipDTO;
import com.example.networkgui.SuperController;
import com.example.networkgui.customWidgets.FriendCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;

public class FriendsController extends SuperController {
    User user;

    public FriendsController() {
        user = loginManager.getLogged();
    }

    @FXML
    private ListView<UserFriendshipDTO> friendshipListView;

    @FXML
    private Label label;

    @FXML
    public void initialize(){
        ObservableList<UserFriendshipDTO> friendships = FXCollections.observableArrayList();
        friendships.setAll(controller.findUserFriendships(user.getEmail()));
        //friendships.setAll(controller.findUserFriendships(user.getEmail()));
        friendshipListView.setCellFactory(new FriendCellFactory());
        //friendshipListView.setCellFactory(new FriendCellFactory2());
        friendshipListView.setItems(friendships);
    }
}
