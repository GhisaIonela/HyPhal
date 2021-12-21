package com.example.networkgui;

import com.company.controller.Controller;
import com.company.service.LoginManager;

public class SuperController {
    protected static Controller controller;
    protected static LoginManager loginManager;

    public static void setController(Controller controller) {
        SuperController.controller = controller;
    }

    public static void setLoginManager(LoginManager loginManager) {
        SuperController.loginManager = loginManager;
    }

    public SuperController(){};
}
