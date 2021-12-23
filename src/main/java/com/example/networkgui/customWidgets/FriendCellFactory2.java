package com.example.networkgui.customWidgets;

import com.company.dto.UserFriendshipDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.io.IOException;

public class FriendCellFactory2 implements Callback<ListView<UserFriendshipDTO>, ListCell<UserFriendshipDTO>> {
    @Override
    public ListCell<UserFriendshipDTO> call(ListView<UserFriendshipDTO> param) {
        return new ListCell<>(){
            @Override
            public void updateItem(UserFriendshipDTO person, boolean empty) {
                super.updateItem(person, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (person != null) {
                    setText(null);
                    try {
                        setText(null);
                        setGraphic(new FXMLLoader(getClass().getResource("friendCell-view.fxml")).load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    setText("null");
                    setGraphic(null);
                }
            }
        };
    }
}