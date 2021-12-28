package com.example.networkgui;

import com.company.domain.Message;
import com.company.domain.User;
import com.company.dto.MessageDTO;
import com.company.dto.UserDTO;
import com.company.dto.UserFriendshipDTO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageToManyController extends SuperController implements Initializable {

    @FXML
    private Button composeButton;
    @FXML
    private TableView<MessageDTO> tableReceived;
    @FXML
    private TableColumn<MessageDTO, String> columnFrom;
    @FXML
    private TableColumn<MessageDTO, String> columnMessageReceived;
    @FXML
    private TableColumn<MessageDTO, String> columnDateReceived;
    @FXML
    private TableColumn<MessageDTO,String> columnId;

    private ObservableList<MessageDTO> modelMessages = FXCollections.observableArrayList();
    private List<Message> messagesList;

    public MessageToManyController() {
        modelMessages.setAll(getMessageDtoList());
        messagesList = controller.getMessagesMultipleUsersForLoggedUser();
    }

    private Message getMessageFromList(String id){
        for (Message m: messagesList) {
            if(m.getId() == Long.parseLong(id)){
                return m;
            }
        }
        return null;
    }

    private List<MessageDTO> getMessageDtoList() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E dd MMM yyyy HH:mm");
        return controller.getMessagesMultipleUsersForLoggedUser()
                .stream()
                .map(m-> new MessageDTO(m.getFrom().getFirstName() + " " + m.getFrom().getLastName(), m.getMessage(), m.getDateTime().format(dtf), String.valueOf(m.getId())))
                .collect(Collectors.toList());
    }

    private void displayDialogViewMessage(Message message){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("dialogMessageReplay-view.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Message info");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            setMessage(message);
            //DialogViewMessageController.setMessage(message);
            DialogViewMessageController dialogViewMessageController = loader.getController();
            //dialogViewMessageController.setMessage(message);
            dialogViewMessageController.setDialogStage(dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HBox createMessageToDisplay(Message message){
        String messageText = message.getMessage();
        Long id = message.getId();
        LocalDateTime dateTime = message.getDateTime();
        User from = message.getFrom();
        List<User> to = message.getTo();
        Message replayTo = message.getReplay();

        Label messageTextLabel = new Label(messageText);
        messageTextLabel.setPrefWidth(100);
        messageTextLabel.setMaxWidth(100);
       // Label idLabel = new Label(String.valueOf(id));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E dd MMM yyyy HH:mm");
        String stringDateTime = dateTime.format(dtf).toString();
        Label dateTimeLabel = new Label(stringDateTime);
        Label fromLabel = new Label(from.getFirstName() + " " + from.getLastName());


        return null;

    }

    public void composeNewMessageToMany(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnFrom.setCellValueFactory(new PropertyValueFactory<>("from"));
        columnMessageReceived.setCellValueFactory(new PropertyValueFactory<>("message"));
        columnDateReceived.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableReceived.setItems(modelMessages);

        tableReceived.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == null) {
                        return;
                    }
                    MessageDTO selected = tableReceived.getSelectionModel().getSelectedItem();
                   // System.out.println(selected.getId() + " " + selected.getFrom());
                   // System.out.println(getMessageFromList(selected.getId()).toString());
                    displayDialogViewMessage(getMessageFromList(selected.getId()));
                }
        );
    }
}
