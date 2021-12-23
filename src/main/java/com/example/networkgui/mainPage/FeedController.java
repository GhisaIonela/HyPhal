package com.example.networkgui.mainPage;

import com.company.domain.User;
import com.example.networkgui.ServiceManager;
import com.example.networkgui.SuperController;

public class FeedController extends SuperController {
    User user;

    public FeedController(){
        user = loginManager.getLogged();
    }
}
