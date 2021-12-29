package com.example.networkgui.customWidgets;

import com.company.domain.FriendRequest;
import com.company.domain.Friendship;
import com.company.domain.User;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;

public class FriendsPageItem {
    private ObjectProperty<User> user;
    private ObjectProperty<FriendRequest> friendRequest;
    private ObjectProperty<Friendship> friendship;
    private ObjectProperty<FriendsPageListViewType>  friendsPageListViewType;

    public FriendsPageItem(User user, FriendRequest friendRequest, Friendship friendship, FriendsPageListViewType friendsPageListViewType) {
        this.user = new SimpleObjectProperty<>();
        this.setUser(user);
        this.friendRequest = new SimpleObjectProperty<>();
        this.setFriendRequest(friendRequest);
        this.friendship = new SimpleObjectProperty<>();
        this.setFriendship(friendship);
        this.friendsPageListViewType = new SimpleObjectProperty<>();
        this.setFriendsPageListViewType(friendsPageListViewType);
    }

    public User getUser() {
        return user.get();
    }

    public ObjectProperty<User> userProperty() {
        return user;
    }

    public void setUser(User user) {
        this.user.set(user);
    }

    public FriendRequest getFriendRequest() {
        return friendRequest.get();
    }

    public ObjectProperty<FriendRequest> friendRequestProperty() {
        return friendRequest;
    }

    public void setFriendRequest(FriendRequest friendRequest) {
        this.friendRequest.set(friendRequest);
    }

    public Friendship getFriendship() {
        return friendship.get();
    }

    public ObjectProperty<Friendship> friendshipProperty() {
        return friendship;
    }

    public void setFriendship(Friendship friendship) {
        this.friendship.set(friendship);
    }

    public FriendsPageListViewType getFriendsPageListViewType() {
        return friendsPageListViewType.get();
    }

    public ObjectProperty<FriendsPageListViewType> friendsPageListViewTypeProperty() {
        return friendsPageListViewType;
    }

    public void setFriendsPageListViewType(FriendsPageListViewType friendsPageListViewType) {
        this.friendsPageListViewType.set(friendsPageListViewType);
    }

    @Override
    public String toString() {
        return "FriendsPageItem{" +
                "user=" + user +
                ", friendRequest=" + friendRequest +
                ", friendship=" + friendship +
                ", friendsPageListViewType=" + friendsPageListViewType +
                '}';
    }

    public static Callback<FriendsPageItem, Observable[]> extractor() {
        return (FriendsPageItem i) -> new Observable[] {
                i.userProperty(),
                i.friendRequestProperty(),
                i.friendshipProperty(),
                i.friendsPageListViewTypeProperty()
        };
    }
}
