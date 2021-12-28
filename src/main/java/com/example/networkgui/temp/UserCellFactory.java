package com.example.networkgui.temp;

import com.company.domain.User;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class UserCellFactory implements Callback<ListView<User>, ListCell<User>> {

    @Override
    public ListCell<User> call(ListView<User> param) {
        return new UserCell();
    }
}
