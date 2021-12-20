package com.example.networkgui;
import com.company.controller.Controller;
import com.company.exceptions.IncorrectPasswordException;
import com.company.exceptions.InvalidEmailExceptions;
import com.company.exceptions.LoginException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class LoginController {
    private Controller controller;
    @FXML
    private Label incorrectEmailLabel;

    @FXML
    private Label incorrectPasswordLabel;

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
            if(controller.isLogged()){
                SceneController.switchToLoggedUserView();
            }
        } catch(InvalidEmailExceptions em){
            incorrectEmailLabel.setText(em.getMessage());
            em.printStackTrace();
        } catch(IncorrectPasswordException ep){
            incorrectPasswordLabel.setText(ep.getMessage());
            ep.printStackTrace();
        }
        catch (LoginException e) {
            loginErrorLabelId.setTextFill(Color.rgb(221,49,49));
            loginErrorLabelId.setText("Incorrect credentials, please try again!");
            e.printStackTrace();
        } catch (IOException ioException){
            ioException.printStackTrace();
        }

    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}