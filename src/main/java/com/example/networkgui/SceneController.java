package com.example.networkgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private static Stage stage;

//    public SceneController(Stage stage){
//        this.stage = stage;
//    }

    public static void setStage(Stage stage2){
        stage = stage2;
    }

    public static void switchToLoggedUserView() throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("loggedUser-view.fxml"));
        //stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
