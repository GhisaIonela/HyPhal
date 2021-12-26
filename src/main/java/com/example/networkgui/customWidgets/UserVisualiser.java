package com.example.networkgui.customWidgets;

import com.company.controller.Controller;
import com.company.domain.FriendRequest;
import com.company.domain.Friendship;
import com.company.domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.time.format.DateTimeFormatter;

public class UserVisualiser extends StackPane {
    private User loggedUser;
    private Controller controller;

    //region Selected user info

    @FXML private Label userNameLabel;

    @FXML private Label userEmailLabel;

    @FXML private Label userCityLabel;

    @FXML private Label userDateOfBirthLabel;

    //endregion

    //region Friend request buttons

    @FXML private StackPane friendRequestButtonStack;

    @FXML private Button sendFriendRequestButton;

    @FXML private Button cancelFriendRequestButton;

    @FXML private HBox friendAcceptAndDeclineButtonBox;

    @FXML private Button acceptFriendRequestButton;

    @FXML private Button declineFriendRequestButton;

    //endregion

    public UserVisualiser(User loggedUser, Controller controller) {
        this.loggedUser = loggedUser;
        this.controller = controller;
    }

    public void setSelectedView() {

    }

    public void setData(User selectedUser){
        userCityLabel.setText(selectedUser.getFirstName() + ' ' + selectedUser.getLastName());
        userEmailLabel.setText(selectedUser.getEmail());
        userCityLabel.setText(selectedUser.getCity());
        userDateOfBirthLabel.setText(selectedUser.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));


    }
}
