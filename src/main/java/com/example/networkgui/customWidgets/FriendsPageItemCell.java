package com.example.networkgui.customWidgets;

import com.company.controller.Controller;
import com.company.domain.User;
import com.example.networkgui.mainPage.FriendsController;
import com.example.networkgui.utils.Icons;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import static com.example.networkgui.customWidgets.FriendsPageListViewType.sentFriendRequest;

public class FriendsPageItemCell extends ListCell<FriendsPageItem> {

    @FXML
    private final BorderPane base = new BorderPane();

    @FXML private final Label nameLabel = new Label();

    @FXML private final Label friendRequestDateTimeLabel =  new Label("Date and time of request");

    @FXML private final Button sendFriendRequestButton = new Button("Add");
    @FXML private final Button cancelFriendRequestButton = new Button("Cancel");
    @FXML private final Button acceptFriendRequestButton = new Button("Accept");
    @FXML private final Button declineFriendRequestButton = new Button("Decline");

    @FXML private final ImageView userIcon = new ImageView(Icons.getInstance().getUserProfileIcon());
    @FXML private final ImageView addIcon = new ImageView(Icons.getInstance().getAddIcon());
    @FXML private final ImageView minusIcon = new ImageView(Icons.getInstance().getMinusIcon());
    @FXML private final ImageView checkedIcon = new ImageView(Icons.getInstance().getCheckedIcon());
    @FXML private final ImageView closeIcon = new ImageView(Icons.getInstance().getCloseIcon());

    public FriendsPageItemCell() {
    }


    @Override
    protected void updateItem(FriendsPageItem item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {

            nameLabel.setText(item.getUser().getFirstName() + ' ' + item.getUser().getLastName());

            userIcon.fitHeightProperty().setValue(45);
            userIcon.preserveRatioProperty().setValue(true);

            addIcon.setFitHeight(15);
            addIcon.preserveRatioProperty().setValue(true);

            minusIcon.setFitHeight(15);
            minusIcon.preserveRatioProperty().setValue(true);

            checkedIcon.setFitHeight(15);
            checkedIcon.preserveRatioProperty().setValue(true);

            closeIcon.setFitHeight(15);
            closeIcon.preserveRatioProperty().setValue(true);


            HBox profile = new HBox();
            profile.getChildren().addAll(userIcon, nameLabel);
            profile.setSpacing(5);
            profile.setAlignment(Pos.CENTER_LEFT);
            base.setLeft(profile);
            base.setRight(null);
            base.setBottom(null);

            sendFriendRequestButton.setGraphic(addIcon);
            cancelFriendRequestButton.setGraphic(closeIcon);
            acceptFriendRequestButton.setGraphic(checkedIcon);
            declineFriendRequestButton.setGraphic(closeIcon);

            switch (item.getFriendsPageListViewType()) {
                case friend:
                    //the base is already set
                    break;
                case receivedFriendRequest:
                    HBox acceptAndDeclineFriendRequestButtonsBox = new HBox();
                    acceptFriendRequestButton.setGraphic(checkedIcon);
                    acceptAndDeclineFriendRequestButtonsBox.getChildren().addAll(acceptFriendRequestButton, declineFriendRequestButton);
                    acceptAndDeclineFriendRequestButtonsBox.setSpacing(5);

                    base.setRight(friendRequestDateTimeLabel);
                    BorderPane.setAlignment(friendRequestDateTimeLabel, Pos.CENTER_RIGHT);
                    base.setBottom(acceptAndDeclineFriendRequestButtonsBox);
                    acceptAndDeclineFriendRequestButtonsBox.setAlignment(Pos.CENTER);

                    break;
                case sentFriendRequest:

                    base.setRight(friendRequestDateTimeLabel);
                    BorderPane.setAlignment(friendRequestDateTimeLabel, Pos.CENTER_RIGHT);
                    base.setBottom(cancelFriendRequestButton);
                    BorderPane.setAlignment(cancelFriendRequestButton, Pos.CENTER);

                    break;
                case user:
                    if (item.getFriendRequest() == null) {
                        base.setRight(sendFriendRequestButton);
                        BorderPane.setAlignment(sendFriendRequestButton, Pos.CENTER);
                    } else
                        base.setRight(null);
                    break;
            }

            setGraphic(base);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}

