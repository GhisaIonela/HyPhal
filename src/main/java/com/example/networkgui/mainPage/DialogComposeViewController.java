package com.example.networkgui.mainPage;

import com.company.exceptions.InvalidEmailExceptions;
import com.example.networkgui.SuperController;
import com.example.networkgui.utils.MessageAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class DialogComposeViewController extends SuperController {

    @FXML private TextField toTextField;
    @FXML private TextArea messageArea;
    @FXML private Button sendButton;
    @FXML private Button cancelButton;

    private Stage dialogStage;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void sendMessage(ActionEvent actionEvent) {
        try{
            String receiversEmails = toTextField.getText();
            String messageBody = messageArea.getText();
            if(messageBody.equals("")){
                throw new IllegalArgumentException("Cannot send empty message");
            }
            List<String> emails = List.of(receiversEmails.split("\s"));
            for (String email:emails) {
                loginManager.verifyEmail(email);
            }
            controller.sendMessageToMultipleUsers(emails, messageBody, null);
        }catch(InvalidEmailExceptions e){
            MessageAlert.showErrorMessage(null, "Please provide valid email addresses separated by space");
        }catch(IllegalArgumentException e){
            MessageAlert.showErrorMessage(null, "Cannot send an empty message");
        }
    }

    public void cancelDialog(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void handleCloseButton(ActionEvent actionEvent) {
        dialogStage.close();
    }
}
