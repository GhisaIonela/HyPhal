package com.example.networkgui.temp;

import com.company.dto.FriendRequestDTO;
import com.example.networkgui.temp.SentFriendRequestCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class SentFriendRequestCellFactory implements Callback<ListView<FriendRequestDTO>, ListCell<FriendRequestDTO>> {

    @Override
    public ListCell<FriendRequestDTO> call(ListView<FriendRequestDTO> param) {
        return new SentFriendRequestCell();
    }
}
