package com.example.networkgui;

import com.company.exceptions.RepositoryDbException;
import com.company.exceptions.UpdatePasswordException;
import com.company.exceptions.UserNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChangePasswordController extends SuperController{
    @FXML
    private Label incorrectEmailLabel;
    @FXML
    private TextField emailFieldId1;
    @FXML
    private Label incorrectConfirmEmailLabel;
    @FXML
    private Label incorrectPasswordLabel;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label incorrectConfirmPassword;
    @FXML
    private TextField emailFieldId;
    @FXML
    private TextField passwordFieldId;
    @FXML
    private Button changePasswordButton;

    public void changePassword(MouseEvent mouseEvent) {
        String email = emailFieldId.getText();
        String confirmEmail = emailFieldId1.getText();
        String password = passwordFieldId.getText();
        String confirmPassword = confirmPasswordField.getText();
        try{
            validate(email, confirmEmail, password, confirmPassword);
            controller.updatePassword(email, password);
            SceneController.switchToAnotherScene("login-view.fxml");
        }catch (UpdatePasswordException ue){
            ue.printStackTrace();
        }catch (RepositoryDbException re){
            incorrectEmailLabel.setText(re.getMessage());
            re.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void validate(String email, String confirmEmail, String password, String confirmPassword){
        List<String> exceptions = new ArrayList<>();

        try{
            controller.findUserByEmail(email);
        }catch(UserNotFoundException e){
            incorrectEmailLabel.setText(e.getMessage());
            exceptions.add(e.getMessage());
        }

        if(email.equals("")){
            incorrectEmailLabel.setText("Email cannot be empty");
            exceptions.add("Email cannot be empty");
        }
        if(confirmEmail.equals("")){
            incorrectConfirmEmailLabel.setText("You must confirm the email address");
            exceptions.add("You must confirm the email address");
        }
        if(!email.equals(confirmEmail)){
            incorrectConfirmEmailLabel.setText("Emails don't match");
        }
        if(password.equals("")){
            incorrectPasswordLabel.setText("You must provide a new password");
            exceptions.add("You must provide a new password");
        }
        if(confirmPassword.equals("")){
            incorrectConfirmPassword.setText("You must confirm the new password");
            exceptions.add("You must provide a new password");
        }
        if(!confirmPassword.equals(password)){
            incorrectConfirmPassword.setText("Passwords don't match");
            exceptions.add("Passwords don't match");
        }

        if(exceptions.size()>0){
            throw new UpdatePasswordException(exceptions.toString());
        }
    }

    public void registerNewUser(MouseEvent mouseEvent) {
        try {
            SceneController.switchToAnotherScene("createAccount-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goBackToLogin(MouseEvent mouseEvent) {
        try {
            SceneController.switchToAnotherScene("login-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleCancelButton(ActionEvent actionEvent) {
        stage.close();
    }

    public void handleMinimizeButton(ActionEvent actionEvent) {
        stage.setIconified(true);
    }
}
