package com.example.networkgui.customWidgets;

import com.company.domain.FriendRequest;
import com.company.domain.Friendship;
import com.company.domain.User;

public class UserFriendsPageDTO {
    private User user;
    private FriendRequest friendRequest;
    private Friendship friendship;
    private FriendsPageListViewType friendsPageListViewType;

    public UserFriendsPageDTO(User user, FriendRequest friendRequest, Friendship friendship, FriendsPageListViewType friendsPageListViewType) {
        this.user = user;
        this.friendRequest = friendRequest;
        this.friendship = friendship;
        this.friendsPageListViewType = friendsPageListViewType;
    }

    public UserFriendsPageDTO(UserFriendsPageDTO userFriendsPageDTO){
        this.user = userFriendsPageDTO.getUser();
        this.friendRequest = userFriendsPageDTO.friendRequest;
        this.friendship = userFriendsPageDTO.friendship;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FriendRequest getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(FriendRequest friendRequest) {
        this.friendRequest = friendRequest;
    }

    public Friendship getFriendship() {
        return friendship;
    }

    public void setFriendship(Friendship friendship) {
        this.friendship = friendship;
    }

    public FriendsPageListViewType getFriendsPageListViewType() {
        return friendsPageListViewType;
    }

    public void setFriendsPageListViewType(FriendsPageListViewType friendsPageListViewType) {
        this.friendsPageListViewType = friendsPageListViewType;
    }

    @Override
    public String toString() {
        return "UserFriendsPageDTO{" +
                "user=" + user +
                ", friendRequest=" + friendRequest +
                ", friendship=" + friendship +
                ", friendsPageListViewType=" + friendsPageListViewType +
                '}';
    }
}
