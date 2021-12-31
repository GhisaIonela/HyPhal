package com.example.networkgui;

import com.company.domain.Message;
import com.company.dto.MessageDTO;
import com.company.events.MessageChangeEvent;
import com.company.observer.Observer;
import com.example.networkgui.mainPage.DialogComposeViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class InboxController extends SuperController implements Initializable, Observer<MessageChangeEvent> {

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

    public InboxController() {
        controller.addObserver(this);
        updateModel();

    }

    private void updateModel(){
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
            loader.setLocation(getClass().getResource("mainPage/dialogMessageReplay-view.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.setTitle("Message info");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            setMessage(message);
            DialogViewMessageController dialogViewMessageController = loader.getController();
            dialogViewMessageController.setDialogStage(dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void composeNewMessageToMany(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("mainPage/dialogCompose-view.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.setTitle("Compose new message");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            DialogComposeViewController dialogComposeViewController = loader.getController();
            dialogComposeViewController.setDialogStage(dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    displayDialogViewMessage(getMessageFromList(selected.getId()));
                }
        );
    }


    @Override
    public void update(MessageChangeEvent messageChangeEvent) {

       updateModel();
    }

    public void handleCancelButton(ActionEvent actionEvent) {
        stage.close();
    }

    public void handleMinimizeButton(ActionEvent actionEvent) {
        stage.setIconified(true);
    }
}
