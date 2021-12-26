package com.example.networkgui;

import com.company.domain.Message;
import com.company.domain.User;
import com.company.service.ConversationManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.rgb;

public class MessageController extends SuperController implements Initializable {
    @FXML
    private ScrollPane users;
    @FXML
    private VBox vbox_users;
    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;
    @FXML
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;

    private ConversationManager chatroom;


    private void createUsersButtons(){
        Iterable<User> userList = controller.findAllUsers();
        for(User user: userList){
            Button userButton = new Button();
            userButton.setText(user.getFirstName() + " " + user.getLastName());
            vbox_users.getChildren().add(userButton);
            userButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    chatroom = controller.createConversation(user.getEmail());
                    loadChatHistory();
                }
            });
        }

    }


    private HBox createNewMsgHBox(Pos alignment, Message message, String style){
        HBox hBox = new HBox();
        hBox.setAlignment(alignment);
        hBox.setPadding(new Insets(5,5,5,10));
        Text text = new Text(message.getMessage());
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle(style);

        textFlow.setPadding(new Insets(5,10,5,10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        if(message.getReplay()!=null){
            Label label = new Label();
            label.setText("Replayed to: ");
            label.setTextFill(rgb(239, 242, 255));
            TextFlow replayWrap = new TextFlow();
            replayWrap.setStyle(style);
            replayWrap.setPadding(new Insets(5,10,5,10));

            Text replayText = new Text(message.getReplay().getMessage());
            TextFlow replay = new TextFlow(replayText);
            replay.setStyle("-fx-color: rgb(239, 242, 255); "+
                    "-fx-background-color: rgb(250, 244, 211);"+
                    "-fx-background-radius: 20px;");
            replay.setPadding(new Insets(5,10,5,10));
            replayText.setFill(rgb(12,22,24));

            VBox bounds = new VBox();
            bounds.getChildren().add(label);
            bounds.getChildren().add(replay);
            bounds.getChildren().add(textFlow);
            replayWrap.getChildren().add(bounds);

            hBox.getChildren().add(replayWrap);
        }
        else{
            hBox.getChildren().add(textFlow);
        }


//        MenuItem replay = new MenuItem("replay");
//        MenuItem delete = new MenuItem("delete");
//        ContextMenu contextMenu = new ContextMenu();
//
//        contextMenu.getItems().add(replay);
//        contextMenu.getItems().add(delete);
//        contextMenu.setStyle("-fx-background-color: grey;");
//
//        Button button = new Button();
//        button.setGraphic(textFlow);
//        button.setContextMenu(contextMenu);
//        button.setStyle("-fx-border-color: transparent;\n" +
//                "    -fx-border-width: 0;\n" +
//                "    -fx-background-radius: 0;\n" +
//                "    -fx-background-color: transparent;");
////        button.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
////            //menuButton.hide();
////            if (newValue) {
////                button.getContextMenu().show(button, Side.BOTTOM, -15, -20);
////            } else {
////                button.getContextMenu().hide();
////            }
////        });
//
//        button.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                button.getContextMenu().show(button, Side.BOTTOM, -15, -20);
//            }
//        });
//        hBox.getChildren().add(button);
        //vbox_messages.getChildren().add(hBox);
        return hBox;
    }



    private void sendMsg(){
        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = tf_message.getText();
                Message message = new Message(null,null, messageToSend);
                if(!messageToSend.isEmpty()){
                    String style = "-fx-color: rgb(239, 242, 255); "+
                            "-fx-background-color: rgb(27,77,62);"+
                            "-fx-background-radius: 20px";
                    HBox hBox = createNewMsgHBox(Pos.CENTER_RIGHT, message, style);
                    vbox_messages.getChildren().add(hBox);

                    //send msg to db
                    chatroom.sendMessage(messageToSend);

                    tf_message.clear();
                }
            }
        });
    }

    private void loadChatHistory(){
        vbox_messages.getChildren().clear();
        List<Message> messages = chatroom.getMessageList();
        for (Message message: messages) {
            if(Objects.equals(message.getFrom().getId(), chatroom.getReceiver().getId()) && !Objects.equals(chatroom.getSender().getId(), chatroom.getReceiver().getId())){
                String style = "-fx-color: rgb(239, 242, 255); "+
                        "-fx-background-color: rgb(209, 172, 0);"+
                        "-fx-background-radius: 20px";
                HBox hMsg = createNewMsgHBox(Pos.CENTER_LEFT, message, style);
                vbox_messages.getChildren().add(hMsg);
            }else{
                String style2 = "-fx-color: rgb(239, 242, 255); "+
                        "-fx-background-color: rgb(27,77,62);"+
                        "-fx-background-radius: 20px";
                HBox hMsg = createNewMsgHBox(Pos.CENTER_RIGHT, message, style2);
                    vbox_messages.getChildren().add(hMsg);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });
        createUsersButtons();
        //changeRoom();
        sendMsg();


    }
}
