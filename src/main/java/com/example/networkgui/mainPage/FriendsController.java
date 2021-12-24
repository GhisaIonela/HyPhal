package com.example.networkgui.mainPage;

import com.company.domain.FriendRequest;
import com.company.domain.User;
import com.company.dto.FriendRequestDTO;
import com.company.dto.UserFriendshipDTO;
import com.example.networkgui.SuperController;
import com.example.networkgui.customWidgets.FriendCellFactory;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.time.format.DateTimeFormatter;

public class FriendsController extends SuperController {
    User loggedUser;
    User selectedUser;
    Boolean isSelectedFriendList = true;
    Boolean isSelectedFriendRequests = false;
    Boolean isSelectedAddFriends = false;

    public FriendsController() {
        loggedUser = loginManager.getLogged();
    }

    @FXML private Label title;

    //region NavigationButtons

    @FXML private Button friendListButton;

    @FXML private Button friendRequestsButton;

    @FXML private Button addFriendsButton;

    //endregion

    //region FriendRequests

    @FXML private StackPane friendRequestsButtonsStack;

    @FXML private Button acceptFriendRequestButton;

    @FXML private Button declineFriendRequestButton;

    @FXML private Button sendFriendRequestButton;

    @FXML private Button cancelFriendRequestButton;

    //endregion

    //region StackListView

    @FXML private StackPane listViewsStackPane;

    @FXML private ListView<UserFriendshipDTO> userFriendshipDTOListView;

    @FXML private ListView<FriendRequestDTO> friendRequestDTOListView;

    @FXML private ListView<User> userListView;

    //endregion

    //region UserVisualiser

    @FXML private Label userName;

    @FXML private Label userEmail;

    @FXML private Label userCity;

    @FXML private Label userDateOfBirth;

    //endregion


    @FXML
    public void initialize(){

        userFriendshipDTOListView.setCellFactory(new FriendCellFactory());

        ObservableList<UserFriendshipDTO> friendships = FXCollections.observableArrayList();
        friendships.setAll(controller.findUserFriendships(loggedUser.getEmail()));

        if (friendships.size() > 0)
        {

            userFriendshipDTOListView.setItems(friendships);

            User firstFriend = controller.findUserById(friendships.get(0).getFriendId());
            userName.setText(firstFriend.getFirstName() + ' ' + firstFriend.getLastName());
            userEmail.setText(firstFriend.getEmail());
            userCity.setText(firstFriend.getCity());
            userDateOfBirth.setText(firstFriend.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }

        userFriendshipDTOListView.toFront();

    }

    @FXML
    public void handleUserSelected(MouseEvent arg0) {

        if(isSelectedFriendList) {
            selectedUser = controller.findUserById(userFriendshipDTOListView.getSelectionModel().getSelectedItem().getFriendId());
        } else if(isSelectedFriendRequests) {
            selectedUser = controller.findUserById(friendRequestDTOListView.getSelectionModel().getSelectedItem().getFromId());
        } else if(isSelectedAddFriends) {
            selectedUser = controller.findUserById(userListView.getSelectionModel().getSelectedItem().getId());
        }
        userName.setText(selectedUser.getFirstName() + ' ' + selectedUser.getLastName());
        userEmail.setText(selectedUser.getEmail());
        userCity.setText(selectedUser.getCity());
        userDateOfBirth.setText(selectedUser.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    @FXML
    public void handleFriendListButtonAction(MouseEvent event) {
        isSelectedFriendList = true;
        isSelectedFriendRequests = false;
        isSelectedAddFriends = false;
        ObservableList<UserFriendshipDTO> friendships = FXCollections.observableArrayList();
        friendships.setAll(controller.findUserFriendships(loggedUser.getEmail()));
        userFriendshipDTOListView.setItems(friendships);
        userFriendshipDTOListView.toFront();
    }

    @FXML
    public void handleFriendRequestsButtonAction(MouseEvent event) {
        isSelectedFriendList = false;
        isSelectedFriendRequests = true;
        isSelectedAddFriends = false;
        ObservableList<FriendRequestDTO> friendRequests = FXCollections.observableArrayList();
        friendRequests.setAll(controller.findReceivedUserFriendRequests(loggedUser.getEmail()));
        friendRequestDTOListView.setItems(friendRequests);
        friendRequestDTOListView.toFront();
    }

    @FXML
    public void handleAddFriendsButtonAction(MouseEvent event) {
        isSelectedFriendList = false;
        isSelectedFriendRequests = false;
        isSelectedAddFriends = true;
        ObservableList<User> users = FXCollections.observableArrayList();
        users.setAll(controller.findNotUserFriends(loggedUser.getEmail()));
        userListView.setItems(users);
        userListView.toFront();

    }

    @FXML
    public void handleAcceptFriendRequestAction(MouseEvent event) {

    }


}
