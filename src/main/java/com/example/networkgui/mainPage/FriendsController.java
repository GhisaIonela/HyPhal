package com.example.networkgui.mainPage;

import com.company.controller.Controller;
import com.company.domain.FriendRequest;
import com.company.domain.FriendRequestStatus;
import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.dto.UserFriendsPageDTO;
import com.company.events.RequestChangeEvent;
import com.company.observer.Observer;
import com.company.utils.FriendsPageListViewType;
import com.example.networkgui.SuperController;
import com.example.networkgui.customWidgets.UserFriendsPageCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendsController extends SuperController implements Observer<RequestChangeEvent> {
    User loggedUser;
    User selectedUser;

    Boolean userWasSelected = false;

    Boolean isSelectedFriendList = true;
    Boolean isSelectedFriendRequests = false;
    Boolean isSelectedReceivedFriendRequests = true;
    Boolean isSelectedSentFriendRequests = false;
    Boolean isSelectedFindUsers = false;

    ObservableList<UserFriendsPageDTO> friendsObservableList = FXCollections.observableArrayList();
    ObservableList<UserFriendsPageDTO> receivedFriendRequestsObservableList = FXCollections.observableArrayList();
    ObservableList<UserFriendsPageDTO> sentFriendRequestsObservableList = FXCollections.observableArrayList();
    ObservableList<UserFriendsPageDTO> usersObservableList = FXCollections.observableArrayList();

    public FriendsController() {
        loggedUser = loginManager.getLogged();
        controller.getFriendRequestService().addObserver(this);
        controller.getFriendshipService().addObserver(this);
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public Controller getController() {
        return controller;
    }

    //region Search
    @FXML private TextField searchTextField;
    @FXML private VBox notFoundViewBox;
    @FXML private VBox notFoundRequestsViewBox;
    //endregion

    //region NavigationButtons
    @FXML private Button friendListNavigationButton;
    @FXML private Button friendRequestsNavigationButton;
    @FXML private Button receivedFriendRequestsNavigationButton;
    @FXML private Button sentFriendRequestsNavigationButton;
    @FXML private Button findUsersNavigationButton;
    //endregion

    //region FriendRequestsActions
    @FXML private StackPane friendRequestsButtonsStack;
    @FXML private HBox friendAcceptAndDeclineButtonsBox;
    @FXML private Button acceptFriendRequestButton;
    @FXML private Button declineFriendRequestButton;
    @FXML private Button sendFriendRequestButton;
    @FXML private Button cancelFriendRequestButton;
    //endregion

    //region StackListViews
    @FXML private StackPane listViewsStackPane;
    @FXML private ListView<UserFriendsPageDTO> friendsListView;
    @FXML private VBox friendRequestsListViewsBox;
    @FXML private ListView<UserFriendsPageDTO> receivedFriendRequestsListView;
    @FXML private ListView<UserFriendsPageDTO> sentFriendRequestsListView;
    @FXML private ListView<UserFriendsPageDTO> usersListView;
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

        //region setting the navigation buttons styles
        setRegionStyle(friendListNavigationButton, "friend-navigation-button-active");
        setRegionStyle(friendRequestsNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(receivedFriendRequestsNavigationButton, "friend-navigation-button-active");
        setRegionStyle(sentFriendRequestsNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(findUsersNavigationButton, "friend-navigation-button-inactive");
        //endregion

        //region setting the custom cell factories for listViews
        friendsListView.setCellFactory(param -> new UserFriendsPageCell(this));
        receivedFriendRequestsListView.setCellFactory(param -> new UserFriendsPageCell(this));
        sentFriendRequestsListView.setCellFactory(param -> new UserFriendsPageCell(this));
        usersListView.setCellFactory(param -> new UserFriendsPageCell(this));
        //endregion

        //setting up noSelectedUserView where user visualiser will load
        noSelectedUserView.toFront();

        //adding listener for search bar
        searchTextField.textProperty().addListener(o -> handleFilter());

        //region setting up the views for no search result found
        notFoundViewBox.setVisible(false);
        notFoundViewBox.toBack();

        notFoundRequestsViewBox.setVisible(false);
        notFoundRequestsViewBox.toBack();
        //endregion

        //region loading up listViews data
        loadListViews();
        //endregion

        //showing friend list as default
        friendListNavigationButton.fire();
    }

    /**
     * Loads the selected user's data and binds it to the friends page
     * @param event mouse event
     */
    @FXML
    public void handleUserSelected(MouseEvent event) {
        UserFriendsPageDTO selectedItem = null;
        if(isSelectedFriendList) {
            selectedItem = friendsListView.getSelectionModel().getSelectedItem();
        } else if(isSelectedFriendRequests) {
            if(isSelectedReceivedFriendRequests) {
                selectedItem = receivedFriendRequestsListView.getSelectionModel().getSelectedItem();
            } else if(isSelectedSentFriendRequests) {
                selectedItem = sentFriendRequestsListView.getSelectionModel().getSelectedItem();
            }
        } else if(isSelectedFindUsers) {
            selectedItem = usersListView.getSelectionModel().getSelectedItem();
        }

        if(selectedItem!=null){
            selectedUser = selectedItem.getUser();
            if(selectedUser != null) {
                updateUserVisualiser();
                if(!userWasSelected)
                {
                    noSelectedUserView.toBack();
                    noSelectedUserView.setVisible(false);
                    noSelectedUserView.setDisable(true);
                    userWasSelected = true;
                }
            }
        }
    }

    public void updateUserVisualiser(){
        userName.setText(selectedUser.getFirstName() + ' ' + selectedUser.getLastName());
        userEmail.setText(selectedUser.getEmail());
        userCity.setText(selectedUser.getCity());
        userDateOfBirth.setText(selectedUser.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        handleFriendRequestsButtons();
    }

    //region Handles for navigation button actions
    /**
     * Shows logged user's friends and changes friends navigation buttons styles accordingly
     * @param event action event
     */
    @FXML
    public void handleFriendListButtonAction(ActionEvent event) {
        isSelectedFriendList = true;
        isSelectedFriendRequests = false;
        isSelectedFindUsers = false;

        //changing button styles
        setRegionStyle(friendListNavigationButton, "friend-navigation-button-active");
        setRegionStyle(friendRequestsNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(findUsersNavigationButton, "friend-navigation-button-inactive");

        friendsListView.toFront();

        searchTextField.clear();
        receivedFriendRequestsListView.getSelectionModel().clearSelection();
        sentFriendRequestsListView.getSelectionModel().clearSelection();
        usersListView.getSelectionModel().clearSelection();
    }

    /**
     * Shows logged user's friend requests and changes friends navigation buttons styles accordingly
     * @param event action event
     */
    @FXML
    public void handleFriendRequestsListButtonAction(ActionEvent event) {
        isSelectedFriendList = false;
        isSelectedFriendRequests = true;
        isSelectedFindUsers = false;

        //changing button styles
        setRegionStyle(friendListNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(friendRequestsNavigationButton, "friend-navigation-button-active");
        setRegionStyle(findUsersNavigationButton, "friend-navigation-button-inactive");

        friendRequestsListViewsBox.toFront();

        if(isSelectedReceivedFriendRequests)
            receivedFriendRequestsListView.toFront();
        else if(isSelectedSentFriendRequests)
            sentFriendRequestsListView.toFront();


        searchTextField.clear();
        friendsListView.getSelectionModel().clearSelection();
        receivedFriendRequestsListView.getSelectionModel().clearSelection();
        sentFriendRequestsListView.getSelectionModel().clearSelection();
        usersListView.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleReceivedFriendRequestsListButtonAction(ActionEvent actionEvent) {
        isSelectedReceivedFriendRequests = true;
        isSelectedSentFriendRequests = false;

        //changing button styles
        setRegionStyle(receivedFriendRequestsNavigationButton, "friend-navigation-button-active");
        setRegionStyle(sentFriendRequestsNavigationButton, "friend-navigation-button-inactive");

        searchTextField.clear();
        friendsListView.getSelectionModel().clearSelection();
        sentFriendRequestsListView.getSelectionModel().clearSelection();
        usersListView.getSelectionModel().clearSelection();

        receivedFriendRequestsListView.toFront();
    }

    @FXML
    public void handleSentFriendRequestsListButtonAction(ActionEvent actionEvent) {
        isSelectedReceivedFriendRequests = false;
        isSelectedSentFriendRequests = true;

        //changing button styles
        setRegionStyle(receivedFriendRequestsNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(sentFriendRequestsNavigationButton, "friend-navigation-button-active");

        searchTextField.clear();
        friendsListView.getSelectionModel().clearSelection();
        receivedFriendRequestsListView.getSelectionModel().clearSelection();
        usersListView.getSelectionModel().clearSelection();

        sentFriendRequestsListView.toFront();
    }

    /**
     * Shows users from database and changes friends navigation buttons styles accordingly
     * @param event action event
     */
    @FXML
    public void handleUsersListButtonAction(ActionEvent event) {
        isSelectedFriendList = false;
        isSelectedFriendRequests = false;
        isSelectedFindUsers = true;

        setRegionStyle(friendListNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(friendRequestsNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(findUsersNavigationButton, "friend-navigation-button-active");

        searchTextField.clear();
        friendsListView.getSelectionModel().clearSelection();
        receivedFriendRequestsListView.getSelectionModel().clearSelection();
        sentFriendRequestsListView.getSelectionModel().clearSelection();

        usersListView.toFront();
    }
    //endregion

    /**
     * Handles accept, deny, send and cancel friend request buttons
     */
    private void handleFriendRequestsButtons(){
        if(selectedUser!=null) {
            FriendRequest friendRequest = controller.findFriendRequest(loggedUser, selectedUser);
            friendRequestsButtonsStack.setVisible(true);
            friendRequestsButtonsStack.setDisable(false);
            if (friendRequest == null) {
                sendFriendRequestButton.setVisible(true);
                sendFriendRequestButton.setDisable(false);
                sendFriendRequestButton.toFront();

                cancelFriendRequestButton.setVisible(false);
                cancelFriendRequestButton.setDisable(true);

                friendAcceptAndDeclineButtonsBox.setVisible(false);
                friendAcceptAndDeclineButtonsBox.setDisable(true);
            } else if (friendRequest.getStatus().equals(FriendRequestStatus.pending) && friendRequest.getIdTo().equals(selectedUser.getId())) {
                sendFriendRequestButton.setVisible(false);
                sendFriendRequestButton.setDisable(true);

                cancelFriendRequestButton.setVisible(true);
                cancelFriendRequestButton.setDisable(false);
                cancelFriendRequestButton.toFront();

                friendAcceptAndDeclineButtonsBox.setVisible(false);
                friendAcceptAndDeclineButtonsBox.setDisable(true);
            } else if (friendRequest.getStatus().equals(FriendRequestStatus.pending) && friendRequest.getIdFrom().equals(selectedUser.getId())) {
                sendFriendRequestButton.setVisible(false);
                sendFriendRequestButton.setDisable(true);

                cancelFriendRequestButton.setVisible(false);
                cancelFriendRequestButton.setDisable(true);

                friendAcceptAndDeclineButtonsBox.setVisible(true);
                friendAcceptAndDeclineButtonsBox.setDisable(false);
                friendAcceptAndDeclineButtonsBox.toFront();
            } else {
                friendRequestsButtonsStack.setVisible(false);
                friendRequestsButtonsStack.setDisable(true);
            }
        }
    }

    /**
     * Handles search bar filters for friends list, friend requests list and users list
     */
    private void handleFilter() {
        notFoundViewBox.setVisible(false);
        notFoundViewBox.toBack();

        notFoundRequestsViewBox.setVisible(false);
        notFoundRequestsViewBox.toBack();

        Predicate<UserFriendsPageDTO> filter = u -> u.getUser().getFirstName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT))
                || u.getUser().getLastName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT));

        if(isSelectedFriendList) {
            friendsListView.setItems(friendsObservableList.filtered(filter));

            if(friendsListView.getItems().size() == 0) {
                notFoundViewBox.setVisible(true);
                notFoundViewBox.toFront();
            }

        } else if(isSelectedFriendRequests) {
            if(isSelectedReceivedFriendRequests) {

                receivedFriendRequestsListView.setItems(receivedFriendRequestsObservableList.filtered(filter));

                if(receivedFriendRequestsListView.getItems().size() == 0) {
                    notFoundRequestsViewBox.setVisible(true);
                    notFoundRequestsViewBox.toFront();
                }

            } else if(isSelectedSentFriendRequests) {

                sentFriendRequestsListView.setItems(sentFriendRequestsObservableList.filtered(filter));

                if(sentFriendRequestsListView.getItems().size() == 0) {
                    notFoundRequestsViewBox.setVisible(true);
                    notFoundRequestsViewBox.toFront();
                }
            }
        } else if(isSelectedFindUsers){

            usersListView.setItems(usersObservableList.filtered(filter));

            if(usersListView.getItems().size() == 0) {
                notFoundViewBox.setVisible(true);
                notFoundViewBox.toFront();
            }

        }
    }

    public void loadListViews() {
        friendsObservableList.setAll(controller.getFriendsForFriendsPage());
        friendsListView.setItems(friendsObservableList);

        receivedFriendRequestsObservableList.setAll(controller.getReceivedFriendRequestsForFriendsPage());
        receivedFriendRequestsListView.setItems(receivedFriendRequestsObservableList);

        sentFriendRequestsObservableList.setAll(controller.getSentFriendRequestsForFriendsPage());
        sentFriendRequestsListView.setItems(sentFriendRequestsObservableList);

        usersObservableList.setAll(controller.getUsersForFriendsPage());
        usersListView.setItems(usersObservableList);
    }

    @FXML
    public void handleSendFriendRequestAction(ActionEvent event) {
        controller.sendFriendRequestAndReturn(loggedUser, selectedUser);
    }

    @FXML
    public void handleCancelFriendRequestAction(ActionEvent event) {
        controller.cancelFriendRequest(loggedUser, selectedUser);
    }

    @FXML
    public void handleAcceptFriendRequestAction(ActionEvent event) {
        controller.acceptFriendRequestAndReturnFriendship(selectedUser, loggedUser);
    }

    @FXML
    public void handleDeclineFriendRequestAction(ActionEvent event) {
        controller.denyFriendRequest(selectedUser, loggedUser);
    }


    @Override
    public void update(RequestChangeEvent requestChangeEvent) {
        loadListViews();
        if(selectedUser!=null)
            updateUserVisualiser();
    }

    public void handleCancelButton(ActionEvent actionEvent) {
        stage.close();
    }

    public void handleMinimizeButton(ActionEvent actionEvent) {
        stage.setIconified(true);
    }
}
