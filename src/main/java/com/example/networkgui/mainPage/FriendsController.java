package com.example.networkgui.mainPage;

import com.company.domain.FriendRequest;
import com.company.domain.FriendRequestStatus;
import com.company.domain.User;
import com.company.dto.FriendRequestDTO;
import com.company.dto.UserFriendshipDTO;
import com.example.networkgui.SuperController;
import com.example.networkgui.customWidgets.FriendCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

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

    ObservableList<UserFriendshipDTO> userFriendshipDTOObservableList = FXCollections.observableArrayList();
    ObservableList<FriendRequestDTO> friendRequestDTOObservableList = FXCollections.observableArrayList();
    ObservableList<User> userObservableList = FXCollections.observableArrayList();

    public FriendsController() {
        loggedUser = loginManager.getLogged();
    }

    @FXML private Label title;

    @FXML private TextField searchTextField;

    //region NavigationButtons

    @FXML private Button friendListNavigationButton;

    @FXML private Button friendRequestsNavigationButton;

    @FXML private Button searchUsersNavigationButton;

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

    @FXML private ListView<User> findUsersListView;

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

        //setting the navigation buttons styles
        setRegionStyle(friendListNavigationButton, "friend-navigation-button-active");
        setRegionStyle(friendRequestsNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(searchUsersNavigationButton, "friend-navigation-button-inactive");

        //setting the custom cell factories for listViews
        userFriendshipDTOListView.setCellFactory(new FriendCellFactory());

        friendListNavigationButton.fire();
        noSelectedUserView.toFront();

        searchTextField.textProperty().addListener(o -> handleFilter(isSelectedFriendList, isSelectedFriendRequests, isSelectedAddFriends));

        userFriendshipDTOObservableList.setAll(controller.findUserFriendships(loggedUser));
        userFriendshipDTOListView.setItems(userFriendshipDTOObservableList);

        friendRequestDTOObservableList.setAll(controller.findReceivedUserFriendRequests(loggedUser));
        friendRequestDTOListView.setItems(friendRequestDTOObservableList);

        userObservableList.setAll(StreamSupport.stream(controller.findAllUsers().spliterator(), false).collect(Collectors.toList()));
        userObservableList.remove(loggedUser);
        findUsersListView.setItems(userObservableList);
    }

    public void setRegionStyle(Region region, String style) {
        region.getStyleClass().clear();
        region.getStyleClass().add(style);
    }

    @FXML
    public void handleUserSelected(MouseEvent event) {

        if(isSelectedFriendList) {
            UserFriendshipDTO userFriendshipDTO =  userFriendshipDTOListView.getSelectionModel().getSelectedItem();
            if(userFriendshipDTO != null)
                selectedUser = controller.findUserById(userFriendshipDTO.getFriendId());
        } else if(isSelectedFriendRequests) {
            FriendRequestDTO friendRequestDTO = friendRequestDTOListView.getSelectionModel().getSelectedItem();
            if(friendRequestDTO != null)
                selectedUser = controller.findUserById(friendRequestDTO.getFromId());
        } else if(isSelectedAddFriends) {
            User user = findUsersListView.getSelectionModel().getSelectedItem();
            if(user != null)
                selectedUser = controller.findUserById(user.getId());
        }

        if(selectedUser != null) {
            userName.setText(selectedUser.getFirstName() + ' ' + selectedUser.getLastName());
            userEmail.setText(selectedUser.getEmail());
            userCity.setText(selectedUser.getCity());
            userDateOfBirth.setText(selectedUser.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

            handleActiveFriendRequestsButtons();

            if(!userWasSelected)
            {
                noSelectedUserView.toBack();
                noSelectedUserView.setOpacity(0);
                noSelectedUserView.setDisable(true);
                userWasSelected = true;
            }
        }
    }

    @FXML
    public void handleFriendListButtonAction(ActionEvent event) {
        isSelectedFriendList = true;
        isSelectedFriendRequests = false;
        isSelectedAddFriends = false;

        setRegionStyle(friendListNavigationButton, "friend-navigation-button-active");
        setRegionStyle(friendRequestsNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(searchUsersNavigationButton, "friend-navigation-button-inactive");

        userFriendshipDTOListView.toFront();
    }

    @FXML
    public void handleFriendRequestsListButtonAction(ActionEvent event) {
        isSelectedFriendList = false;
        isSelectedFriendRequests = true;
        isSelectedAddFriends = false;

        setRegionStyle(friendListNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(friendRequestsNavigationButton, "friend-navigation-button-active");
        setRegionStyle(searchUsersNavigationButton, "friend-navigation-button-inactive");

        friendRequestDTOListView.toFront();
    }

    @FXML
    public void handleUsersListButtonAction(ActionEvent event) {
        isSelectedFriendList = false;
        isSelectedFriendRequests = false;
        isSelectedAddFriends = true;

        setRegionStyle(friendListNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(friendRequestsNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(searchUsersNavigationButton, "friend-navigation-button-active");

        findUsersListView.toFront();
    }

    private void handleActiveFriendRequestsButtons(){
        if(selectedUser!=null) {

        }
        FriendRequest friendRequest = controller.findFriendRequest(loggedUser,selectedUser);
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
            userFriendshipDTOListView.setItems(userFriendshipDTOObservableList.filtered(p));
        } else if(isSelectedFriendRequests) {
            Predicate<FriendRequestDTO> p = f -> f.getFromFirstName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT))
                    || f.getFromLastName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT));
            friendRequestDTOListView.setItems(friendRequestDTOObservableList.filtered(p));
        } else if(isSelectedAddFriends){
            Predicate<User> p = u -> u.getFirstName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT))
                    || u.getLastName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT));
            findUsersListView.setItems(userObservableList.filtered(p));
        }
    }

    @FXML
    public void handleAcceptFriendRequestAction(ActionEvent event) {

    }


}
