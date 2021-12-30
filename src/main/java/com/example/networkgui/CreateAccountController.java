package com.example.networkgui;

import com.company.domain.User;
import com.company.exceptions.CreateAccountException;
import com.company.exceptions.InvalidEmailExceptions;
import com.company.exceptions.ServiceException;

import com.company.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateAccountController extends SuperController{

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
        LocalDate dateOfBirth= dateOfBirthPicker.getValue();
        LocalDateTime localDateTime = null;
        if(dateOfBirth!=null){
            localDateTime = dateOfBirth.atTime(00,00,00);
            localDateTime.format(Constants.DATE_OF_BIRTH_FORMATTER);
        }

        try{
            validate(email, password, confirmPassword, firstName, lastName, city, dateOfBirth);
            User user = controller.createAccount(email, firstName, lastName, city, localDateTime, password);
            if(user == null){
                SceneController.switchToAnotherScene("login-view.fxml");
            }
        } catch (ServiceException se){
        incorrectEmailLabel.setText(se.getMessage());
        se.printStackTrace();
        }catch (CreateAccountException e) {
            e.printStackTrace();
        }catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void validate(String email, String password, String confirmPassword, String firstName, String lastName, String city, LocalDate dateOfBirth){
        List<String> exceptions = new ArrayList<>();

        if(!password.equals(confirmPassword)) {
            incorrectConfirmPasswordLabel.setText("Passwords don't match");
            exceptions.add("Passwords don't match");
        }
        if(password.equals("")){
            incorrectPasswordLabel.setText("You must provide a password");
            exceptions.add("You must provide a password");
        }
        if(confirmPassword.equals("")){
            incorrectConfirmPasswordLabel.setText("You must confirm the password");
            exceptions.add("You must confirm the password");
        }
        if(Objects.equals(email, "")){
            incorrectEmailLabel.setText("Email cannot be empty");
            exceptions.add("Email cannot be empty");
        }
        try{
            loginManager.verifyEmail(email);
        }catch (InvalidEmailExceptions e){
            incorrectEmailLabel.setText(e.getMessage());
            exceptions.add(e.getMessage());
        }
        if(Objects.equals(firstName, "")){
            incorrectFirstNameLabel.setText("First name cannot be empty");
            exceptions.add("First name cannot be empty");
        }
        if(Objects.equals(lastName, "")){
            incorrectLastNameLabel.setText("Last name cannot be empty");
            exceptions.add("Last name cannot be empty");
        }
        if(Objects.equals(city, "")){
            incorrectCityLabel.setText("City cannot be empty");
            exceptions.add("City cannot be empty");
        }
        if(dateOfBirth==null) {
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

    public void handleMinimizeButton(ActionEvent actionEvent) {
        stage.setIconified(true);
    }

    public void handleCancelButton(ActionEvent actionEvent) {
        stage.close();
    }
}
