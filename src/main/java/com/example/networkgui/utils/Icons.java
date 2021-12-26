package com.example.networkgui.utils;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Icons {
    private final Image userProfileIcon = new Image("images/favpng_user-profile-icon-design.png");
    private final Image addIcon = new Image("images/add.png");
    private final Image minusIcon = new Image("images/minus.png");
    private final Image checkedIcon = new Image("images/checked.png");
    private final Image closeIcon = new Image("images/close.png");

    private static final Icons instance = new Icons();
    private Icons() {}

    public static Icons getInstance(){
        return instance;
    }

    public Image getUserProfileIcon() {
        return userProfileIcon;
    }

    public Image getAddIcon() {
        return addIcon;
    }

    public Image getMinusIcon() {
        return minusIcon;
    }

    public Image getCheckedIcon() {
        return checkedIcon;
    }

    public Image getCloseIcon() {
        return closeIcon;
    }
}
