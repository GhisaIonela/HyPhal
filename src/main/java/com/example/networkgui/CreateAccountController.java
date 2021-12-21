package com.example.networkgui;

import com.company.controller.Controller;
import com.company.domain.User;
import com.company.exceptions.CreateAccountException;
import com.company.exceptions.InvalidEmailExceptions;
import com.company.exceptions.ServiceException;
import com.company.service.LoginManager;
import com.company.utils.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreateAccountController {
    private Controller controller;
    private LoginManager loginManager;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerButton;
    @FXML
    private Label incorrectEmailLabel;
    @FXML
    private Label incorrectPasswordLabel;
    @FXML
    private Button loginNowButton;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label incorrectConfirmPasswordLabel;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField cityField;
    @FXML
    private Label incorrectFirstNameLabel;
    @FXML
    private Label incorrectLastNameLabel;
    @FXML
    private Label incorrectCityLabel;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private Label incorrectDatePicker;

    public void createAccount(MouseEvent mouseEvent) {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String city = cityField.getText();
        LocalDateTime dateOfBirth = null;
        if(dateOfBirthPicker.getValue()!=null)
        {
            dateOfBirth = LocalDateTime.from(dateOfBirthPicker.getValue());
            dateOfBirth.format(Constants.DATE_OF_BIRTH_FORMATTER);
        }

        try{
            validate(email, password, confirmPassword, firstName, lastName, city, dateOfBirth);
            User user = controller.createAccount(email, firstName, lastName, city, dateOfBirth, password);
            if(user == null){
                SceneController.switchToAnotherScene("login-view.fxml");
            }
        }catch (CreateAccountException e){
            e.printStackTrace();
        }catch (ServiceException se){
            incorrectEmailLabel.setText(se.getMessage());
            se.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void validate(String email, String password, String confirmPassword, String firstName, String lastName, String city, LocalDateTime dateOfBirth){
        List<String> exceptions = new ArrayList<>();
        if(!password.equals(confirmPassword)) {
            incorrectConfirmPasswordLabel.setText("Passwords don't match");
            exceptions.add("Passwords don't match");
        }
        if(email==null){
            incorrectEmailLabel.setText("Email cannot be empty");
            exceptions.add("Email cannot be empty");
        }
        try{
            loginManager.verifyEmail(email);
        }catch (InvalidEmailExceptions e){
            incorrectEmailLabel.setText(e.getMessage());
            exceptions.add(e.getMessage());
        }
        if(firstName==null){
            incorrectFirstNameLabel.setText("First name cannot be empty");
            exceptions.add("First name cannot be empty");
        }
        if(lastName==null){
            incorrectLastNameLabel.setText("Last name cannot be empty");
            exceptions.add("Last name cannot be empty");
        }
        if(city==null){
            incorrectCityLabel.setText("City cannot be empty");
            exceptions.add("City cannot be empty");
        }
        if(dateOfBirth==null){
            incorrectDatePicker.setText("Date of birth cannot be empty");
            exceptions.add("Date of birth cannot be empty");
        }
        if (exceptions.size()>0){
            throw new CreateAccountException(exceptions.toString());
        }
    }

    public void goToLoginView(MouseEvent mouseEvent) {
        try {
            SceneController.switchToAnotherScene("login-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
