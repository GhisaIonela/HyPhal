package com.example.networkgui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class SceneController {
    private static Stage stage;

    public static void setStage(Stage stage2){
        stage = stage2;
    }

    public static void switchToAnotherScene(String sceneFxmlFile) throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource(sceneFxmlFile));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static Object getControllerForView(String sceneFxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(SceneController.class.getResource(sceneFxmlFile));
        fxmlLoader.load();
        return fxmlLoader.getController();
    }

}
