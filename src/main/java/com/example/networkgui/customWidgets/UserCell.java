package com.example.networkgui.customWidgets;

import com.company.domain.User;
import com.company.dto.UserFriendshipDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class UserCell extends ListCell<User> {

    @FXML
    private Label nameLabel = new Label();

    public UserCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userCell-view.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            nameLabel.setText(item.getFirstName() + ' ' + item.getLastName());

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}