package com.example.networkgui.mainPage;

import com.company.controller.Controller;
import com.example.networkgui.ServiceManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainPageApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPage-view.fxml"));
        AnchorPane root = loader.load();

        ServiceManager serviceManager = new ServiceManager();

        MainPageController mainPageController = loader.getController();
        mainPageController.setUser(serviceManager.getController().findUserById(315L));
        mainPageController.setService(serviceManager);
        mainPageController.setDefaultPage(FXMLLoader.load(SettingsController.class.getResource("feed-view.fxml")));

        stage.setScene(new Scene(root));
        stage.setTitle("HyPhal");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
