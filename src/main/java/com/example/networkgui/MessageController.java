package com.example.networkgui;

import com.company.domain.Message;
import com.company.domain.User;
import com.company.dto.UserDTO;
import com.company.listen.Listener;
import com.company.service.ConversationManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.postgresql.PGNotification;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    @FXML
    private TableView<UserDTO> tableUsers;
    @FXML
    private TableColumn<UserDTO, String> columnUser;
    @FXML
    private TextField searchUserTextField;
    @FXML
    private Label chatWithLabel;

    private ConversationManager chatroom;
    private ObservableList<UserDTO> modelUser = FXCollections.observableArrayList();

    public MessageController() {
        modelUser.setAll(getUserDTOList());
    }

    private void listenToNewMessageForCurrentRoom() {
        try {
            Listener messageListener = new Listener(connection, "message"){
                @Override
                public void handleNotification(PGNotification notification){
                    String message = notification.getParameter();
                    Pattern p = Pattern.compile("\\:(.*?)\\,");
                    Matcher m = p.matcher(message);
                    List<String> tokens = new ArrayList<>();
                    while(m.find()){
                        tokens.add(m.group(1));
                    }
                    if(tokens!=null && chatroom!=null){
                        Long msgId = Long.parseLong(tokens.get(0));
                        System.out.println(msgId);
                        Long idFrom = Long.parseLong(tokens.get(1));
                        System.out.println(idFrom);
                        Message message1 = chatroom.findMessage(msgId);
                        if(idFrom == chatroom.getReceiver().getId()){
                            if(message1.getTo().contains(chatroom.getSender())){
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("da1");
                                        loadChatHistory();
                                        System.out.println("da2");
                                    }
                                });

                            }
                        }

                    }
                }
            };
            messageListener.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private HBox createNewMsgHBox(Pos alignment, Message message, String style){
        HBox hBox = new HBox();
        hBox.setAlignment(alignment);
        hBox.setPadding(new Insets(5,5,5,10));
        Text text = new Text(message.getMessage());
        text.setFont(new Font(16));
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle(style);

        textFlow.setPadding(new Insets(5,10,5,10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        TextFlow dateTime = displayMessageDateTime(message, style);

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
            bounds.getChildren().add(dateTime);

            replayWrap.getChildren().add(bounds);

            hBox.getChildren().add(replayWrap);
        }
        else{
            VBox bounds = new VBox();
            bounds.setStyle(style);
            bounds.getChildren().add(textFlow);
            bounds.getChildren().add(dateTime);
            hBox.getChildren().add(bounds);
        }

       messageOptionPopup(textFlow);

        return hBox;
    }

    private void messageOptionPopup(TextFlow textFlow){
        VBox options = new VBox();
        ImageView replayImg = new ImageView(new Image("images/replay.png"));
        replayImg.setFitHeight(15);
        replayImg.setFitWidth(15);
        Button replay = new Button();
        replay.setStyle("-fx-background-color: transparent");
        replay.setGraphic(replayImg);

        ImageView deleteImg = new ImageView(new Image("images/delete.png"));
        deleteImg.setFitHeight(15);
        deleteImg.setFitWidth(15);
        Button delete = new Button();
        delete.setStyle("-fx-background-color: transparent");
        delete.setGraphic(deleteImg);

        options.getChildren().add(replay);
        options.getChildren().add(delete);

        replay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("replay pressed");
                replayToMsg(textFlow);
            }
        });

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            }
        });

        createPopOver(textFlow,options);

    }

    public static void createPopOver(Node hoverableNode, Node contentNode) {
        PopOver popOver = new PopOver();
        popOver.setContentNode(contentNode);
        final Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500)));
        timeline.setOnFinished(finishEvent -> {
            if (hoverableNode.isHover() || contentNode.isHover()){
                timeline.play();
            }
            else{
                popOver.hide();
            }
        });
        hoverableNode.setOnMouseEntered(mouseEvent -> {if (!popOver.isShowing()) popOver.show(hoverableNode);});
        hoverableNode.setOnMouseExited(mouseEvent -> timeline.play());
    }

    private void replayToMsg(TextFlow textFlow){
        String indexString = null;
        if(textFlow.getParent().getParent() instanceof HBox){
            indexString = textFlow.getParent().getId();
        }else{
            indexString = textFlow.getParent().getParent().getParent().getId();
        }

        if(indexString==null){
            indexString = String.valueOf(chatroom.getMessageList().size()-1);
        }
        int msgIndex = Integer.parseInt(indexString);

        Popup popupReplay = new Popup();
        TextField textField = new TextField();
        HBox bound = new HBox();

        ImageView replayImg = new ImageView(new Image("images/send.png"));
        replayImg.setFitHeight(10);
        replayImg.setFitWidth(10);
        Button sendReplay = new Button();
        sendReplay.setGraphic(replayImg);

        Button cancel = new Button();
        ImageView imageView = new ImageView();
        imageView.setImage(new Image("images/cancel.png"));

        imageView.setFitHeight(10);
        imageView.setFitWidth(10);
        cancel.setGraphic(imageView);
        bound.getChildren().add(textField);
        bound.getChildren().add(sendReplay);
        bound.getChildren().add(cancel);
        popupReplay.getContent().add(bound);


        Point2D point = textFlow.localToScene(0.0,  0.0);
        popupReplay.setX(stage.getX() + point.getX());
        popupReplay.setY(stage.getY() + point.getY());

        popupReplay.show(stage);

        sendReplay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String msg = textField.getText();
                if(!Objects.equals(msg, "")){
                    chatroom.replayToMessage(msgIndex, msg);
                    popupReplay.hide();

                    loadChatHistory();
                }
            }
        });

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popupReplay.hide();
            }
        });


    }

    private TextFlow displayMessageDateTime(Message message, String style){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E HH:mm");
        String dateTime = message.getDateTime().format(dtf).toString();

        Text text = new Text(dateTime);
        text.setFill(Color.color(0.934, 0.945, 0.996));
        text.setFont(new Font(11));
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(5,10,5,10));
        textFlow.setStyle(style);

        return textFlow;
    }

    private void sendMsg(){
        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = tf_message.getText();
                Message message = new Message(null,null, messageToSend);
                message.setDateTime(LocalDateTime.now());
                if(!messageToSend.isEmpty()){
                    String style = "-fx-color: rgb(239, 242, 255); "+
                            "-fx-background-color: rgb(27,77,62);"+
                            "-fx-background-radius: 20px";
                    HBox hBox = createNewMsgHBox(Pos.CENTER_RIGHT, message, style);
                    vbox_messages.getChildren().add(hBox);
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
            String index = String.valueOf(messages.indexOf(message));
            if(Objects.equals(message.getFrom().getId(), chatroom.getReceiver().getId()) && !Objects.equals(chatroom.getSender().getId(), chatroom.getReceiver().getId())){
                String style = "-fx-color: rgb(239, 242, 255); "+
                        "-fx-background-color: rgb(209, 172, 0);"+
                        "-fx-background-radius: 20px";
                HBox hMsg = createNewMsgHBox(Pos.CENTER_LEFT, message, style);
                hMsg.setId(index);
                vbox_messages.getChildren().add(hMsg);
            }else{
                String style2 = "-fx-color: rgb(239, 242, 255); "+
                        "-fx-background-color: rgb(27,77,62);"+
                        "-fx-background-radius: 20px";
                HBox hMsg = createNewMsgHBox(Pos.CENTER_RIGHT, message, style2);
                hMsg.setId(index);
                vbox_messages.getChildren().add(hMsg);
            }
        }
    }

    private void customSendButton(){
        ImageView imageView = new ImageView(new Image("images/send.png"));
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        button_send.setGraphic(imageView);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customSendButton();
        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });
        columnUser.setPrefWidth(tableUsers.getPrefWidth() - 2);
        columnUser.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("fullInfo"));
        tableUsers.setItems(modelUser);
        searchUserTextField.textProperty().addListener(o->handleFilter());
        sendMsg();
        listenToNewMessageForCurrentRoom();
        initChatRoom();


    }

    private void handleFilter() {
        Predicate<UserDTO> predicate = u -> u.getFullName().startsWith(searchUserTextField.getText());
        modelUser.setAll(getUserDTOList()
                .stream().filter(predicate)
                .collect(Collectors.toList()));
    }

    private List<UserDTO> getUserDTOList() {
        List<User> userList = StreamSupport.stream(controller.findAllUsers().spliterator(), false)
                .collect(Collectors.toList());
        return userList
                .stream()
                .map(u-> new UserDTO(u.getEmail(), u.getFirstName(), u.getLastName()))
                .collect(Collectors.toList());
    }

    private void initChatRoom(){
        tableUsers.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == null) {
                        return;
                    }
                    chatWithLabel.setText(newValue.getFullInfo().split("\n")[0]);
                    chatroom = controller.createConversation(newValue.getFullInfo().split("\n")[1]);
                    loadChatHistory();

                }
        );

    }

}
