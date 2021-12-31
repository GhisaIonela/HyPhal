package com.example.networkgui;
import com.company.exceptions.IncorrectPasswordException;
import com.company.exceptions.InvalidEmailExceptions;
import com.company.exceptions.LoginException;
import com.example.networkgui.mainPage.MainPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import java.io.IOException;

public class LoginController extends SuperController{
    @FXML
    private BorderPane mainPage;
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

    public LoginController() {};

    @FXML
    protected void onLoginButtonClicked() {
        loginErrorLabelId.setText("");
        incorrectEmailLabel.setText("");
        incorrectPasswordLabel.setText("");
        try{
            controller.login(emailFieldId.getText(), passwordFieldId.getText());
            if(controller.isLogged()){
                SceneController.switchToAnotherScene("mainPage/mainPage-view.fxml");
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

    public void registerNewUser(MouseEvent onMouseClicked) {
        try {
            SceneController.switchToAnotherScene("createAccount-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToChangePassword(MouseEvent mouseEvent) {
        try {
            SceneController.switchToAnotherScene("changePassword-view.fxml");
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

    public void handleDragWindow(MouseEvent mouseEvent) {
        mainPage.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - mouseEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - mouseEvent.getSceneY());
        });
    }
}