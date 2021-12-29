package com.company.service;

import com.company.domain.FriendRequest;
import com.company.domain.FriendRequestStatus;
import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.events.ChangeEventType;
import com.company.events.RequestChangeEvent;
import com.company.exceptions.ServiceException;
import com.company.observer.Observable;
import com.company.observer.Observer;
import com.company.repository.db.FriendRequestsDbRepository;
import com.company.repository.db.FriendshipDbRepository;
import com.company.repository.db.UserDbRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendRequestService implements Observable<RequestChangeEvent> {
    private UserDbRepository userDbRepository;
    private FriendshipDbRepository friendshipDbRepository;
    private FriendRequestsDbRepository friendRequestsDbRepository;

    private List<Observer<RequestChangeEvent>> observers = new ArrayList<>();


    public FriendRequestService(UserDbRepository userDbRepository, FriendshipDbRepository friendshipDbRepository, FriendRequestsDbRepository friendRequestsDbRepository){
        this.userDbRepository = userDbRepository;
        this.friendshipDbRepository = friendshipDbRepository;
        this.friendRequestsDbRepository = friendRequestsDbRepository;
    }

    public List<Observer<RequestChangeEvent>> getObservers() {
        return observers;
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

    public FriendRequest sendFriendRequestAndReturn(Long idFrom, Long idTo){
        if(userDbRepository.findOne(idFrom) == null || userDbRepository.findOne(idTo) == null)
            throw new ServiceException("The users do not exist");
        FriendRequest friendRequest = findFriendRequest(idFrom, idTo);
        if(friendRequest == null) {
            friendRequest = friendRequestsDbRepository.saveAndReturn(new FriendRequest(idFrom, idTo));
            notifyObservers(new RequestChangeEvent(ChangeEventType.ADD));
        } else {
            if(friendRequest.getStatus() == FriendRequestStatus.accepted) {
                throw new ServiceException("The friend request was already accepted");
            }
            else if(friendRequest.getStatus() == FriendRequestStatus.pending) {
                throw new ServiceException("There already exists a friend request that is pending");
            }
            else {
                friendRequest.setIdFrom(idFrom);
                friendRequest.setIdTo(idTo);
                friendRequest.setStatus(FriendRequestStatus.pending);
                friendRequestsDbRepository.update(friendRequest);
                notifyObservers(new RequestChangeEvent(ChangeEventType.UPDATE));
            }

        }
        return friendRequest;
    }

    public FriendRequest cancelFriendRequest(Long idFrom, Long idTo){
        FriendRequest friendRequest = friendRequestsDbRepository.findOne(idFrom, idTo);
        if(friendRequest == null)
            throw new ServiceException("The friend request you want to cancel does not exist");
        if(friendRequest.getStatus() != FriendRequestStatus.pending)
            throw new ServiceException("The friend request can no longer be cancelled");
        FriendRequest friendRequest1 =  friendRequestsDbRepository.delete(friendRequest.getId());
        notifyObservers(new RequestChangeEvent(ChangeEventType.DELETE));
        return friendRequest1;
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

    public Friendship acceptFriendRequestAndReturnFriendShip(Long idFrom, Long idTo){
        FriendRequest friendRequest = friendRequestsDbRepository.findOne(idFrom, idTo);
        if(friendRequest == null)
            throw new ServiceException("The friend request you want to accept does not exist");
        if(friendRequest.getStatus() != FriendRequestStatus.pending)
            throw new ServiceException("The friend request can no longer be accepted");
        friendRequest.setStatus(FriendRequestStatus.accepted);
        Friendship savedFriendship = friendshipDbRepository.saveAndReturn(new Friendship(idFrom, idTo));
        friendRequestsDbRepository.update(friendRequest);
        if(savedFriendship != null) {
            notifyObservers(new RequestChangeEvent(ChangeEventType.ADD));
        }
        return savedFriendship;
    }

    public FriendRequest denyFriendRequest(Long idFrom, Long idTo){
        FriendRequest friendRequest = friendRequestsDbRepository.findOne(idFrom, idTo);
        if(friendRequest == null)
            throw new ServiceException("The friend request you want to deny does not exist");
        if(friendRequest.getStatus() != FriendRequestStatus.pending)
            throw new ServiceException("The friend request can no longer be denied");
        friendRequest.setStatus(FriendRequestStatus.denied);
        FriendRequest friendRequest1 =  friendRequestsDbRepository.update(friendRequest);
        notifyObservers(new RequestChangeEvent(ChangeEventType.UPDATE));
        return friendRequest;
    }

    @Override
    public void addObserver(Observer<RequestChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<RequestChangeEvent> e) {

    }

    @Override
    public void notifyObservers(RequestChangeEvent requestChangeEvent) {
        observers.forEach(requestChangeEventObserver -> requestChangeEventObserver.update(requestChangeEvent));
    }
}
