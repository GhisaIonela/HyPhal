package com.example.networkgui;
import com.company.controller.Controller;
import com.company.exceptions.LoginException;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    private Controller controller;

    @FXML
    private Label loginErrorLabelId;

    @FXML
    private TextField emailFieldId;

    @FXML
    private PasswordField passwordFieldId;

    @FXML
    private Button loginButtonId;

    @FXML
    protected void onLoginButtonClicked() {
        try{
            controller.login(emailFieldId.getText(), passwordFieldId.getText());
        } catch (LoginException e) {
            loginErrorLabelId.setText("Incorrect credentials, please try again!");
            e.printStackTrace();
        }

    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}