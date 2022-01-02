package com.example.networkgui;

import com.company.controller.Controller;
import com.company.domain.Message;
import com.company.service.LoginManager;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.sql.Connection;

public abstract class SuperController {
    protected static Controller controller;
    protected static LoginManager loginManager;
    protected static Stage stage;
    protected static Connection connection;
    protected static Message message;
    protected static Listener dbListener;

    public static void setDbListener(Listener dbListener) {
        SuperController.dbListener = dbListener;
    }

    public void setMessage(Message message1){
        message = message1;
    }

    public static void setConnection(Connection connection1){
        connection = connection1;
    }

    public static void setStage(Stage stage1){
        stage = stage1;
    }

    public static void setController(Controller controller) {
        SuperController.controller = controller;
    }

    public static void setLoginManager(LoginManager loginManager) {
        SuperController.loginManager = loginManager;
    }

    public SuperController(){};

    /**
     * Clears the previous StyleClass of a given stage region and adds the given StyleClass
     * !!!The region must already have a stylesheet set!!!
     * @param region the given region
     * @param styleClassName the name of the StyleClass to be set
     */
    protected void setRegionStyle(Region region, String styleClassName) {
        region.getStyleClass().clear();
        region.getStyleClass().add(styleClassName);
    }


}
