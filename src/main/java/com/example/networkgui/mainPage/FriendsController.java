package com.example.networkgui.mainPage;

import com.company.domain.FriendRequest;
import com.company.domain.FriendRequestStatus;
import com.company.domain.User;
import com.company.dto.FriendRequestDTO;
import com.company.dto.UserFriendshipDTO;
import com.example.networkgui.SuperController;
import com.example.networkgui.customWidgets.ReceivedFriendRequestCellFactory;
import com.example.networkgui.customWidgets.SentFriendRequestCellFactory;
import com.example.networkgui.customWidgets.FriendCellFactory;
import com.example.networkgui.customWidgets.UserCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendsController extends SuperController {
    User loggedUser;
    User selectedUser;

    Boolean userWasSelected = false;

    Boolean isSelectedFriendList = true;
    Boolean isSelectedFriendRequests = false;
    Boolean isSelectedReceivedFriendRequests = true;
    Boolean isSelectedSentFriendRequests = false;
    Boolean isSelectedFindUsers = false;

    ObservableList<UserFriendshipDTO> userFriendshipDTOObservableList = FXCollections.observableArrayList();
    ObservableList<FriendRequestDTO> receivedFriendRequestDTOObservableList = FXCollections.observableArrayList();
    ObservableList<FriendRequestDTO> sentFriendRequestDTOObservableList = FXCollections.observableArrayList();
    ObservableList<User> userObservableList = FXCollections.observableArrayList();

    public FriendsController() {
        loggedUser = loginManager.getLogged();
    }

    //region Search

    @FXML private TextField searchTextField;

    @FXML private VBox notFoundViewBox;

    @FXML private  VBox notFoundRequestsViewBox;

    //
    //region NavigationButtons

    @FXML private Button friendListNavigationButton;

    @FXML private Button friendRequestsNavigationButton;

    @FXML private Button receivedFriendRequestsNavigationButton;

    @FXML private Button sentFriendRequestsNavigationButton;

    @FXML private Button findUsersNavigationButton;

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

    @FXML private VBox friendRequestsListViewsBox;

    @FXML private ListView<FriendRequestDTO> receivedFriendRequestDTOListView;

    @FXML private ListView<FriendRequestDTO> sentFriendRequestDTOListView;

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
        setRegionStyle(receivedFriendRequestsNavigationButton, "friend-navigation-button-active");
        setRegionStyle(sentFriendRequestsNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(findUsersNavigationButton, "friend-navigation-button-inactive");

        //setting the custom cell factories for listViews
        userFriendshipDTOListView.setCellFactory(new FriendCellFactory());
        receivedFriendRequestDTOListView.setCellFactory(new ReceivedFriendRequestCellFactory());
        sentFriendRequestDTOListView.setCellFactory(new SentFriendRequestCellFactory());
        findUsersListView.setCellFactory(new UserCellFactory());

        //showing friend list as default
        friendListNavigationButton.fire();

        //showing noSelectedUserView where user visualiser will load
        noSelectedUserView.toFront();

        //adding listener for search bar
        searchTextField.textProperty().addListener(o -> handleFilter());

        //region loading up listViews data
        userFriendshipDTOObservableList.setAll(controller.findUserFriendships(loggedUser));
        userFriendshipDTOListView.setItems(userFriendshipDTOObservableList);

        receivedFriendRequestDTOObservableList.setAll(controller.findReceivedUserFriendRequests(loggedUser));
        receivedFriendRequestDTOListView.setItems(receivedFriendRequestDTOObservableList);

        sentFriendRequestDTOObservableList.setAll(controller.findSentUserFriendRequests(loggedUser));
        sentFriendRequestDTOListView.setItems(sentFriendRequestDTOObservableList);

        userObservableList.setAll(StreamSupport.stream(controller.findAllUsers().spliterator(), false).collect(Collectors.toList()));
        userObservableList.remove(loggedUser);
        findUsersListView.setItems(userObservableList);
        //endregion

        notFoundViewBox.setOpacity(0);
        notFoundViewBox.toBack();

        notFoundRequestsViewBox.setOpacity(0);
        notFoundRequestsViewBox.toBack();
    }

    /**
     * Clears the previous StyleClass of a given stage region and adds the given StyleClass
     * The region must already have a stylesheet set!!!
     * @param region the given region
     * @param styleClassName the name of the StyleClass to be set
     */
    public void setRegionStyle(Region region, String styleClassName) {
        region.getStyleClass().clear();
        region.getStyleClass().add(styleClassName);
    }


    /**
     * Loads the selected user's data and binds it to the friends page
     * @param event mouse event
     */
    @FXML
    public void handleUserSelected(MouseEvent event) {
        if(isSelectedFriendList) {
            UserFriendshipDTO userFriendshipDTO =  userFriendshipDTOListView.getSelectionModel().getSelectedItem();
            if(userFriendshipDTO != null)
                selectedUser = controller.findUserById(userFriendshipDTO.getFriendId());
        } else if(isSelectedFriendRequests) {
            if(isSelectedReceivedFriendRequests) {
                FriendRequestDTO friendRequestDTO = receivedFriendRequestDTOListView.getSelectionModel().getSelectedItem();
                if (friendRequestDTO != null)
                    selectedUser = controller.findUserById(friendRequestDTO.getFromId());
            } else if(isSelectedSentFriendRequests) {
                FriendRequestDTO friendRequestDTO = sentFriendRequestDTOListView.getSelectionModel().getSelectedItem();
                if (friendRequestDTO != null)
                    selectedUser = controller.findUserById(friendRequestDTO.getToId());
            }
        } else if(isSelectedFindUsers) {
            User user = findUsersListView.getSelectionModel().getSelectedItem();
            if(user != null)
                selectedUser = controller.findUserById(user.getId());
        }

        if(selectedUser != null) {
            userName.setText(selectedUser.getFirstName() + ' ' + selectedUser.getLastName());
            userEmail.setText(selectedUser.getEmail());
            userCity.setText(selectedUser.getCity());
            userDateOfBirth.setText(selectedUser.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

            handleFriendRequestsButtons();

            if(!userWasSelected)
            {
                noSelectedUserView.toBack();
                noSelectedUserView.setOpacity(0);
                noSelectedUserView.setDisable(true);
                userWasSelected = true;
            }
        }
    }

    /**
     * Shows logged user's friends and changes friends navigation buttons styles accordingly
     * @param event action event
     */
    @FXML
    public void handleFriendListButtonAction(ActionEvent event) {
        isSelectedFriendList = true;
        isSelectedFriendRequests = false;
        isSelectedFindUsers = false;

        setRegionStyle(friendListNavigationButton, "friend-navigation-button-active");
        setRegionStyle(friendRequestsNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(findUsersNavigationButton, "friend-navigation-button-inactive");

        userFriendshipDTOListView.toFront();

        searchTextField.clear();
        receivedFriendRequestDTOListView.getSelectionModel().clearSelection();
        sentFriendRequestDTOListView.getSelectionModel().clearSelection();
        findUsersListView.getSelectionModel().clearSelection();
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

        setRegionStyle(friendListNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(friendRequestsNavigationButton, "friend-navigation-button-active");
        setRegionStyle(findUsersNavigationButton, "friend-navigation-button-inactive");

        friendRequestsListViewsBox.toFront();

        if(isSelectedReceivedFriendRequests)
            receivedFriendRequestDTOListView.toFront();
        else if(isSelectedSentFriendRequests)
            sentFriendRequestDTOListView.toFront();


        searchTextField.clear();
        userFriendshipDTOListView.getSelectionModel().clearSelection();
        receivedFriendRequestDTOListView.getSelectionModel().clearSelection();
        sentFriendRequestDTOListView.getSelectionModel().clearSelection();
        findUsersListView.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleReceivedFriendRequestsListButtonAction(ActionEvent actionEvent) {
        isSelectedReceivedFriendRequests = true;
        isSelectedSentFriendRequests = false;

        setRegionStyle(receivedFriendRequestsNavigationButton, "friend-navigation-button-active");
        setRegionStyle(sentFriendRequestsNavigationButton, "friend-navigation-button-inactive");

        receivedFriendRequestDTOListView.toFront();

        searchTextField.clear();
        userFriendshipDTOListView.getSelectionModel().clearSelection();
        sentFriendRequestDTOListView.getSelectionModel().clearSelection();
        findUsersListView.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleSentFriendRequestsListButtonAction(ActionEvent actionEvent) {
        isSelectedReceivedFriendRequests = false;
        isSelectedSentFriendRequests = true;

        setRegionStyle(receivedFriendRequestsNavigationButton, "friend-navigation-button-inactive");
        setRegionStyle(sentFriendRequestsNavigationButton, "friend-navigation-button-active");

        sentFriendRequestDTOListView.toFront();

        searchTextField.clear();
        userFriendshipDTOListView.getSelectionModel().clearSelection();
        receivedFriendRequestDTOListView.getSelectionModel().clearSelection();
        findUsersListView.getSelectionModel().clearSelection();
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

        findUsersListView.toFront();

        searchTextField.clear();
        userFriendshipDTOListView.getSelectionModel().clearSelection();
        receivedFriendRequestDTOListView.getSelectionModel().clearSelection();
        sentFriendRequestDTOListView.getSelectionModel().clearSelection();
    }

    /**
     * Handles accept, deny, send and cancel friend request buttons
     */
    private void handleFriendRequestsButtons(){
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

    /**
     * Handles search bar filters for friends list, friend requests list and users list
     */
    private void handleFilter() {
        notFoundViewBox.setOpacity(0);
        notFoundViewBox.toBack();

        notFoundRequestsViewBox.setOpacity(0);
        notFoundRequestsViewBox.toBack();

        if(isSelectedFriendList) {
            Predicate<UserFriendshipDTO> p = u -> u.getFirstName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT))
                    || u.getLastName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT));
            userFriendshipDTOListView.setItems(userFriendshipDTOObservableList.filtered(p));

            if(userFriendshipDTOListView.getItems().size() == 0) {
                notFoundViewBox.setOpacity(1);
                notFoundViewBox.toFront();
            }

        } else if(isSelectedFriendRequests) {
            if(isSelectedReceivedFriendRequests) {
                Predicate<FriendRequestDTO> p = f -> f.getFromFirstName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT))
                        || f.getFromLastName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT));
                receivedFriendRequestDTOListView.setItems(receivedFriendRequestDTOObservableList.filtered(p));

                if(receivedFriendRequestDTOListView.getItems().size() == 0) {
                    notFoundRequestsViewBox.setOpacity(1);
                    notFoundRequestsViewBox.toFront();
                }

            } else if(isSelectedSentFriendRequests) {
                Predicate<FriendRequestDTO> p = f -> f.getToFirstName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT))
                        || f.getToLastName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT));
                sentFriendRequestDTOListView.setItems(sentFriendRequestDTOObservableList.filtered(p));

                if(sentFriendRequestDTOListView.getItems().size() == 0) {
                    notFoundRequestsViewBox.setOpacity(1);
                    notFoundRequestsViewBox.toFront();
                }
            }
        } else if(isSelectedFindUsers){
            Predicate<User> p = u -> u.getFirstName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT))
                    || u.getLastName().toLowerCase(Locale.ROOT).startsWith(searchTextField.getText().toLowerCase(Locale.ROOT));
            findUsersListView.setItems(userObservableList.filtered(p));

            if(findUsersListView.getItems().size() == 0) {
                notFoundViewBox.setOpacity(1);
                notFoundViewBox.toFront();
            }

        }
    }

    @FXML
    public void handleAcceptFriendRequestAction(ActionEvent event) {

    }


}
