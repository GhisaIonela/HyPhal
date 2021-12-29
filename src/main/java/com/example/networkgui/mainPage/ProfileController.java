package com.example.networkgui.mainPage;

import com.company.domain.User;
import com.example.networkgui.SuperController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;

public class ProfileController extends SuperController {
    User user;

    @FXML
    private Label name;

    @FXML
    private Label email;

    @FXML
    private Label city;

    @FXML
    private Label dateOfBirth;

    public ProfileController() {
        user = loginManager.getLogged();
    }

    @FXML
    public void initialize() {
        name.setText(user.getFirstName() + ' ' + user.getLastName());
        email.setText(user.getEmail());
        city.setText(user.getCity());
        dateOfBirth.setText(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd-MM-YYYY")));
    }
}
