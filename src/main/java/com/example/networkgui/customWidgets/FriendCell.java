package com.example.networkgui.customWidgets;

import com.company.dto.UserFriendshipDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class FriendCell extends ListCell<UserFriendshipDTO> {

    @FXML
    private Label nameLabel = new Label();

    @FXML
    private Label friendshipDateLabel =  new Label();

    public FriendCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("friendCell-view.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(UserFriendshipDTO item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            nameLabel.setText(item.getFirstName() + ' ' + item.getLastName());
            friendshipDateLabel.setText(item.getDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
