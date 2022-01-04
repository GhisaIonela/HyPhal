package com.example.networkgui.mainPage;

import com.company.exceptions.InvalidEmailExceptions;
import com.example.networkgui.SuperController;
import com.example.networkgui.utils.MessageAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.List;

public class DialogComposeViewController extends SuperController {

    @FXML private TextField toTextField;
    @FXML private TextArea messageArea;
    @FXML private Button sendButton;
    @FXML private Button cancelButton;
    @FXML private Label feedbackForSendMessageButtonLabel;

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
            setFeedbackLabelStyle(true,"Message sent successfully");
        }catch(InvalidEmailExceptions e){
            setFeedbackLabelStyle(false,"Please provide valid email addresses separated by space");
        }catch(IllegalArgumentException e){
            setFeedbackLabelStyle(false,"Cannot send an empty message");
        }
    }

    public void setFeedbackLabelStyle(boolean isSuccessful, String text) {
        feedbackForSendMessageButtonLabel.setText(text);

        if (isSuccessful) {
            feedbackForSendMessageButtonLabel.setTextFill(Paint.valueOf("LIGHTGREEN"));
            feedbackForSendMessageButtonLabel.setStyle("-fx-border-color: LIGHTGREEN;");
        } else {
            feedbackForSendMessageButtonLabel.setTextFill(Paint.valueOf("RED"));
            feedbackForSendMessageButtonLabel.setStyle("-fx-border-color: RED");
        }

        feedbackForSendMessageButtonLabel.setVisible(true);
    }

    public void cancelDialog(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void handleCloseButton(ActionEvent actionEvent) {
        dialogStage.close();
    }
}
