package com.example.networkgui.customWidgets;

import com.company.dto.FriendRequestDTO;
import com.company.dto.UserFriendshipDTO;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ReceivedFriendRequestCellFactory implements Callback<ListView<FriendRequestDTO>, ListCell<FriendRequestDTO>> {

    @Override
    public ListCell<FriendRequestDTO> call(ListView<FriendRequestDTO> param) {
        return new ReceivedFriendRequestCell();
    }
}