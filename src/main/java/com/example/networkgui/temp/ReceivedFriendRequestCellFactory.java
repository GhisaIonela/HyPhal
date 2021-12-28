package com.example.networkgui.temp;

import com.company.dto.FriendRequestDTO;
import com.example.networkgui.temp.ReceivedFriendRequestCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ReceivedFriendRequestCellFactory implements Callback<ListView<FriendRequestDTO>, ListCell<FriendRequestDTO>> {

    @Override
    public ListCell<FriendRequestDTO> call(ListView<FriendRequestDTO> param) {
        return new ReceivedFriendRequestCell();
    }
}