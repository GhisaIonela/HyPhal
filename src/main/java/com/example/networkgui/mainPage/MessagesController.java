package com.example.networkgui.mainPage;

import com.company.domain.User;
import com.example.networkgui.ServiceManager;
import com.example.networkgui.SuperController;

public class MessagesController extends SuperController {
    User user;

    public MessagesController() {
        user = loginManager.getLogged();
    }
}
