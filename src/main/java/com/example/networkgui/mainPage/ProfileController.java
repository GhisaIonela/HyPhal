package com.example.networkgui.mainPage;

import com.company.controller.Controller;
import com.company.domain.User;
import com.example.networkgui.ServiceManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;

public class ProfileController {
    private User user;
    private ServiceManager serviceManager;

    @FXML
    private Label firstName;

    @FXML
    private Label lastName;

    @FXML
    private Label email;

    @FXML
    private Label city;

    @FXML
    private Label dateOfBirth;

    public void setUser(User user) {
        this.user = user;
    }

    public void setService(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @FXML
    public void initialize() {
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        city.setText(user.getCity());
        dateOfBirth.setText(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd-MM-YYYY")));
    }
}
