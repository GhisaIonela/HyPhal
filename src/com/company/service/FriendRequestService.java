package com.company.service;

import com.company.domain.FriendRequest;
import com.company.domain.FriendRequestStatus;
import com.company.exceptions.ServiceException;
import com.company.repository.db.FriendRequestsDbRepository;
import com.company.repository.db.FriendshipDbRepository;
import com.company.repository.db.UserDbRepository;

public class FriendRequestService {
    private UserDbRepository userDbRepository;
    private FriendshipDbRepository friendshipDbRepository;
    private FriendRequestsDbRepository friendRequestsDbRepository;

    public FriendRequestService(UserDbRepository userDbRepository, FriendshipDbRepository friendshipDbRepository, FriendRequestsDbRepository friendRequestsDbRepository){
        this.userDbRepository = userDbRepository;
        this.friendshipDbRepository = friendshipDbRepository;
        this.friendRequestsDbRepository = friendRequestsDbRepository;
    }

    public Iterable<FriendRequest> findAll(){
        return friendRequestsDbRepository.findAll();
    }

    public FriendRequest sendFriendRequest(Long idFrom, Long idTo){
        if(userDbRepository.findOne(idFrom) == null || userDbRepository.findOne(idTo) == null)
            throw new ServiceException("The users do not exist");
        if(friendRequestsDbRepository.findOne(idFrom, idTo)!=null)
            throw new ServiceException("You already send a friend request to this user");
        return friendRequestsDbRepository.save(new FriendRequest(idFrom, idTo));
    }

    public FriendRequest cancelFriendRequest(Long idFrom, Long idTo){
        FriendRequest friendRequest = friendRequestsDbRepository.findOne(idFrom, idTo);
        if(friendRequest == null)
            throw new ServiceException("You did not send a friend request to this user yet");
        if(friendRequest.getStatus() == FriendRequestStatus.accepted)
            throw new ServiceException("The friend request was already accepted, it can not be canceled anymore");
        return friendRequestsDbRepository.delete(friendRequest.getId());
    }

    public FriendRequest acceptFriendRequest(Long idFrom, Long idTo){
        FriendRequest friendRequest = friendRequestsDbRepository.findOne(idFrom, idTo);
        if(friendRequest == null)
            throw new ServiceException("The friend request you want to accept does not exist");
        friendRequest.setStatus(FriendRequestStatus.accepted);
        return friendRequestsDbRepository.update(friendRequest);
    }

    public FriendRequest denyFriendRequest(Long idFrom, Long idTo){
        FriendRequest friendRequest = friendRequestsDbRepository.findOne(idFrom, idTo);
        if(friendRequest == null)
            throw new ServiceException("The friend request you want to deny does not exist");
        friendRequest.setStatus(FriendRequestStatus.denied);
        return friendRequestsDbRepository.update(friendRequest);
    }
}
