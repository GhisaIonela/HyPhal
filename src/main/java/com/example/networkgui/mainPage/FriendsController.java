package com.example.networkgui.mainPage;

import com.company.domain.FriendRequest;
import com.company.domain.FriendRequestStatus;
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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendsController extends SuperController {
    User loggedUser;
    User selectedUser;
    Boolean userWasSelected = false;
    Boolean isSelectedFriendList = true;
    Boolean isSelectedFriendRequests = false;
    Boolean isSelectedAddFriends = false;

    public FriendsController() {
        loggedUser = loginManager.getLogged();
    }

    @FXML private Label title;

    @FXML private TextField searchTextField;

    //region NavigationButtons

    @FXML private Button friendListButton;

    @FXML private Button friendRequestsButton;

    @FXML private Button addFriendsButton;

    //endregion

    //region FriendRequests

    @FXML private StackPane friendRequestsButtonsStack;

    @FXML private HBox friendAcceptAndDeclineButtonsBox;

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

    @FXML private AnchorPane noSelectedUserView;

    //endregion


    @FXML
    public void initialize(){

        userFriendshipDTOListView.setCellFactory(new FriendCellFactory());

        friendListButton.fire();
        noSelectedUserView.toFront();

        searchTextField.textProperty().addListener(o -> handleFilter(isSelectedFriendList, isSelectedFriendRequests, isSelectedAddFriends));

        /*
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

            FriendRequest friendRequest = controller.findFriendRequest(loggedUser.getEmail(),firstFriend.getEmail());
            if(friendRequest == null) {
                friendRequestsButtonsStack.setDisable(false);
                friendRequestsButtonsStack.setOpacity(1);
                sendFriendRequestButton.toFront();
            }else if(friendRequest.getStatus() == FriendRequestStatus.accepted) {
                friendRequestsButtonsStack.setDisable(true);
                friendRequestsButtonsStack.setOpacity(0);
            } else if(friendRequest.getStatus() == FriendRequestStatus.pending) {
                friendRequestsButtonsStack.setDisable(false);
                friendRequestsButtonsStack.setOpacity(1);
                if (Objects.equals(friendRequest.getIdFrom(), loggedUser.getId()))
                    cancelFriendRequestButton.toFront();
                else if (Objects.equals(friendRequest.getIdTo(), loggedUser.getId())) {
                    friendAddAndDeclineButtonsBox.toFront();
                }
            }

        }

        friendListButton.setTextFill(Color.valueOf("#D0ECE7"));
        friendRequestsButton.setTextFill(Color.valueOf("black"));
        addFriendsButton.setTextFill(Color.valueOf("black"));

        userFriendshipDTOListView.toFront();

         */

    }

    @FXML
    public void handleUserSelected(MouseEvent event) {

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

        handleActiveFriendRequestsButtons();

        if(selectedUser!=null && !userWasSelected)
        {
            noSelectedUserView.toBack();
            noSelectedUserView.setOpacity(0);
            noSelectedUserView.setDisable(true);
            userWasSelected = true;
        }
    }

    @FXML
    public void handleFriendListButtonAction(ActionEvent event) {
        isSelectedFriendList = true;
        isSelectedFriendRequests = false;
        isSelectedAddFriends = false;

        friendListButton.setTextFill(Color.valueOf("#D0ECE7"));
        friendRequestsButton.setTextFill(Color.valueOf("black"));
        addFriendsButton.setTextFill(Color.valueOf("black"));

        ObservableList<UserFriendshipDTO> friendships = FXCollections.observableArrayList();
        friendships.setAll(controller.findUserFriendships(loggedUser.getEmail()));
        userFriendshipDTOListView.setItems(friendships);
        userFriendshipDTOListView.toFront();
    }

    @FXML
    public void handleFriendRequestsListButtonAction(ActionEvent event) {
        isSelectedFriendList = false;
        isSelectedFriendRequests = true;
        isSelectedAddFriends = false;

        friendListButton.setTextFill(Color.valueOf("black"));
        friendRequestsButton.setTextFill(Color.valueOf("#D0ECE7"));
        addFriendsButton.setTextFill(Color.valueOf("black"));

        ObservableList<FriendRequestDTO> friendRequests = FXCollections.observableArrayList();
        friendRequests.setAll(controller.findReceivedUserFriendRequests(loggedUser.getEmail()));
        friendRequestDTOListView.setItems(friendRequests);
        friendRequestDTOListView.toFront();
    }

    @FXML
    public void handleUsersListButtonAction(ActionEvent event) {
        isSelectedFriendList = false;
        isSelectedFriendRequests = false;
        isSelectedAddFriends = true;

        friendListButton.setTextFill(Color.valueOf("black"));
        friendRequestsButton.setTextFill(Color.valueOf("black"));
        addFriendsButton.setTextFill(Color.valueOf("#D0ECE7"));

        ObservableList<User> users = FXCollections.observableArrayList();
        users.setAll(StreamSupport.stream(controller.findAllUsers().spliterator(), false).collect(Collectors.toList()));
        userListView.setItems(users);
        userListView.toFront();
    }

    private void handleActiveFriendRequestsButtons(){
        FriendRequest friendRequest = controller.findFriendRequest(loggedUser.getEmail(),selectedUser.getEmail());
        friendRequestsButtonsStack.setOpacity(1);
        friendRequestsButtonsStack.setDisable(false);
        if(friendRequest==null) {
            sendFriendRequestButton.setOpacity(1);
            sendFriendRequestButton.setDisable(false);
            sendFriendRequestButton.toFront();

            cancelFriendRequestButton.setOpacity(0);
            cancelFriendRequestButton.setDisable(true);

            friendAcceptAndDeclineButtonsBox.setOpacity(0);
            friendAcceptAndDeclineButtonsBox.setDisable(true);
        } else if(friendRequest.getStatus().equals(FriendRequestStatus.pending) && friendRequest.getIdTo().equals(selectedUser.getId())){
            sendFriendRequestButton.setOpacity(0);
            sendFriendRequestButton.setDisable(true);

            cancelFriendRequestButton.setOpacity(1);
            cancelFriendRequestButton.setDisable(false);
            cancelFriendRequestButton.toFront();

            friendAcceptAndDeclineButtonsBox.setOpacity(0);
            friendAcceptAndDeclineButtonsBox.setDisable(true);
        } else if(friendRequest.getStatus().equals(FriendRequestStatus.pending) && friendRequest.getIdFrom().equals(selectedUser.getId())){
            sendFriendRequestButton.setOpacity(0);
            sendFriendRequestButton.setDisable(true);

            cancelFriendRequestButton.setOpacity(0);
            cancelFriendRequestButton.setDisable(true);

            friendAcceptAndDeclineButtonsBox.setOpacity(1);
            friendAcceptAndDeclineButtonsBox.setDisable(false);
            friendAcceptAndDeclineButtonsBox.toFront();
        } else {
            friendRequestsButtonsStack.setOpacity(0);
            friendRequestsButtonsStack.setDisable(true);
        }
    }

    private void handleFilter(boolean isSelectedFriendList, boolean isSelectedFriendRequests, boolean isSelectedAddFriends) {

        if(isSelectedFriendList) {
            Predicate<UserFriendshipDTO> p = u -> u.getFirstName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT))
                    || u.getLastName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT));
            ObservableList<UserFriendshipDTO> userFriendshipDTOS = FXCollections.observableArrayList();
            userFriendshipDTOS.setAll(StreamSupport.stream(controller.findUserFriendships(loggedUser.getEmail()).spliterator(), false)
                            .filter(p).collect(Collectors.toList()));
            userFriendshipDTOListView.setItems(userFriendshipDTOS);
        } else if(isSelectedFriendRequests) {
            Predicate<FriendRequestDTO> p = f -> f.getFromFirstName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT))
                    || f.getFromLastName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT));
            ObservableList<FriendRequestDTO> friendRequestDTOS = FXCollections.observableArrayList();
            friendRequestDTOS.setAll(StreamSupport.stream(controller.findReceivedUserFriendRequests(loggedUser.getEmail()).spliterator(), false)
                    .filter(p).collect(Collectors.toList()));
            friendRequestDTOListView.setItems(friendRequestDTOS);
        } else if(isSelectedAddFriends){
            Predicate<User> p = u -> u.getFirstName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT))
                    || u.getLastName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT));
            ObservableList<User> userObservableList = FXCollections.observableArrayList();
            userObservableList.setAll(StreamSupport.stream(controller.findAllUsers().spliterator(), false)
                    .filter(p).collect(Collectors.toList()));
            userListView.setItems(userObservableList);
        }
    }

    @FXML
    public void handleAcceptFriendRequestAction(ActionEvent event) {

    }


}
