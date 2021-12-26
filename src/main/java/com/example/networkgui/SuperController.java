package com.example.networkgui;

import com.company.controller.Controller;
import com.company.service.LoginManager;
import javafx.stage.Stage;

public abstract class SuperController {
    protected static Controller controller;
    protected static LoginManager loginManager;
    protected static Stage stage;

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
}
