package com.example.networkgui.temp;

import com.company.dto.FriendRequestDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class ReceivedFriendRequestCell extends ListCell<FriendRequestDTO> {

    @FXML
    private Label nameLabel = new Label();

    @FXML
    private Label dateTimeOfFriendRequest = new Label();

    @FXML
    private Button acceptButton = new Button();

    @FXML
    private Button denyButton = new Button();

    public ReceivedFriendRequestCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("receivedFriendRequestCell-view.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(FriendRequestDTO item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            nameLabel.setText(item.getFromFirstName() + ' ' + item.getFromLastName());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
