package com.company.service;

import com.company.domain.FriendRequest;
import com.company.domain.FriendRequestStatus;
import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.exceptions.ServiceException;
import com.company.repository.db.FriendRequestsDbRepository;
import com.company.repository.db.FriendshipDbRepository;
import com.company.repository.db.UserDbRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public FriendRequest findOne(Long idFrom, Long idTo){
        return friendRequestsDbRepository.findOne(idFrom, idTo);
    }

    public User getSender(FriendRequest friendRequest) {
        return userDbRepository.findOne(friendRequest.getIdFrom());
    }

    public User getReceiver(FriendRequest friendRequest) {
        return userDbRepository.findOne(friendRequest.getIdTo());
    }

    public FriendRequest findFriendRequest(Long idUser1, Long idUser2) {
        FriendRequest friendRequest = friendRequestsDbRepository.findOne(idUser1, idUser2);
        if(friendRequest!=null)
            return friendRequest;
        return friendRequestsDbRepository.findOne(idUser2, idUser1);
    }

    public FriendRequest sendFriendRequest(Long idFrom, Long idTo){
        if(userDbRepository.findOne(idFrom) == null || userDbRepository.findOne(idTo) == null)
            throw new ServiceException("The users do not exist");
        if(friendRequestsDbRepository.findOne(idFrom, idTo)!=null)
            throw new ServiceException("You already send a friend request to this user");
        if(friendRequestsDbRepository.findOne(idTo, idFrom)!=null)
            throw new ServiceException("This user already send you a friend request");
        return friendRequestsDbRepository.save(new FriendRequest(idFrom, idTo));
    }

    public FriendRequest cancelFriendRequest(Long idFrom, Long idTo){
        FriendRequest friendRequest = friendRequestsDbRepository.findOne(idFrom, idTo);
        if(friendRequest == null)
            throw new ServiceException("The friend request you want to cancel does not exist");
        if(friendRequest.getStatus() != FriendRequestStatus.pending)
            throw new ServiceException("The friend request can no longer be cancelled");
        return friendRequestsDbRepository.delete(friendRequest.getId());
    }

    public FriendRequest acceptFriendRequest(Long idFrom, Long idTo){
        FriendRequest friendRequest = friendRequestsDbRepository.findOne(idFrom, idTo);
        if(friendRequest == null)
            throw new ServiceException("The friend request you want to accept does not exist");
        if(friendRequest.getStatus() != FriendRequestStatus.pending)
            throw new ServiceException("The friend request can no longer be accepted");
        friendRequest.setStatus(FriendRequestStatus.accepted);
        friendshipDbRepository.save(new Friendship(idFrom, idTo));
        return friendRequestsDbRepository.update(friendRequest);
    }

    public FriendRequest denyFriendRequest(Long idFrom, Long idTo){
        FriendRequest friendRequest = friendRequestsDbRepository.findOne(idFrom, idTo);
        if(friendRequest == null)
            throw new ServiceException("The friend request you want to deny does not exist");
        if(friendRequest.getStatus() != FriendRequestStatus.pending)
            throw new ServiceException("The friend request can no longer be denied");
        friendRequest.setStatus(FriendRequestStatus.denied);
        return friendRequestsDbRepository.update(friendRequest);
    }
}
