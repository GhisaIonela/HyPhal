package com.example.networkgui.mainPage;

import com.company.domain.User;
import com.example.networkgui.ServiceManager;

public class FeedController {
    private User user;
    private ServiceManager serviceManager;

    public void setUser(User user) {
        this.user = user;
    }

    public void setService(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
}
