package com.example.networkgui;

import com.company.domain.Message;
import com.company.domain.User;
import com.company.dto.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DialogViewMessageController extends SuperController{
    @FXML
    private TextFlow fromTextFlow;
    @FXML
    private TextFlow toTextFlow;
    @FXML
    private TextFlow dateTextFlow;
    @FXML
    private TextFlow ReplayedToTextFlow;
    @FXML
    private TextFlow messageTextFlow;
    @FXML
    private TextArea textAreaAnswer;
    @FXML
    private Button replayButton;
    @FXML
    private Button replayAllButton;
    @FXML
    private Button cancelButton;

    private Stage dialogStage;

    public void setDialogStage(Stage stage){
        this.dialogStage = stage;
        setFields();
    }

    @FXML
    private void initialize(){
//        setFields();
    }

    private void setFields(){
        Text from = new Text(new UserDTO(message.getFrom().getEmail(), message.getFrom().getFirstName(), message.getFrom().getLastName()).toString());
        fromTextFlow.getChildren().add(from);
        List<String> emails = message.getTo().stream().map(User::getEmail).collect(Collectors.toList());
        String emailsString = "";
        for (String email: emails) {
            emailsString = emailsString + " " + email;
        }
        Text to = new Text(emailsString);
        toTextFlow.getChildren().add(to);
        Text msgText = new Text(message.getMessage());
        messageTextFlow.getChildren().add(msgText);
        Message repl = message.getReplay();
        Text replText;
        if(repl != null){
            replText = new Text(repl.getFrom().getEmail() + "\n" + repl.getMessage());
        }else{
            replText = new Text("no replay");
        }
        ReplayedToTextFlow.getChildren().add(replText);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E dd MMM yyyy HH:mm");
        Text dateText = new Text(message.getDateTime().format(dtf));
        dateTextFlow.getChildren().add(dateText);

    }

    @FXML
    public void replay(ActionEvent actionEvent) {
        String text = textAreaAnswer.getText();
        List<String> emails = new ArrayList<>();
        emails.add(message.getFrom().getEmail());
        if(!Objects.equals(text, "")){
            controller.sendMessageToMultipleUsers(emails, text, message.getId());
        }else{
            //throw exception to do
            System.out.println("mesaj gol");
        }

    }

    @FXML
    public void replayAll(ActionEvent actionEvent) {
        String text = textAreaAnswer.getText();
        List<String> emails = new ArrayList<>();
        emails.add(message.getFrom().getEmail());
        emails.addAll(message.getTo().stream().map(User::getEmail).collect(Collectors.toList()));
        emails.remove(loginManager.getLogged().getEmail());

        if(!Objects.equals(text, "")){
            controller.sendMessageToMultipleUsers(emails, text, message.getId());
        }else{
            //throw exception to do
            System.out.println("mesaj gol");
        }
    }

    @FXML
    public void cancelDialog(ActionEvent actionEvent) {
        dialogStage.close();
    }
}
